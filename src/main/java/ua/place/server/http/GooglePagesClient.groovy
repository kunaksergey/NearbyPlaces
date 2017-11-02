package ua.place.server.http

import groovyx.net.http.HTTPBuilder

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45,38&radius=500&key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg
import ua.place.entity.place.DetailPlace
import ua.place.entity.quary.Request
import ua.place.server.config.Config
import ua.place.server.enumer.StatusCodeEnum
import ua.place.server.exception.GoogleException

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

class GooglePagesClient {
    // final static Logger logger = Logger.getLogger(GooglePagesClient.class)
    def http = new HTTPBuilder(Config.BASE_URL)

    def requestOnePage(request) throws GoogleException {
        assert request instanceof Request
        return quaryGoogle(request, null)
    }

    def requestAllPages(request) throws GoogleException {
        assert request instanceof Request
        def googlePages = []
        def next_page_token = request.next_page_token //токен от клиента

        def lastRequestTimestamp=0
        while (next_page_token != null) {
            def p = Config.PAUSE - (System.currentTimeMillis() - lastRequestTimestamp)
            sleep(p)
            def page = quaryGoogle(request, next_page_token)
            googlePages += page
            next_page_token = page[0].next_page_token //токен от сервера
            lastRequestTimestamp = System.currentTimeMillis()
        }
        return googlePages

    }

    def quaryGoogle(request, next_page_token) throws GoogleException {
        def countFail = Config.MAX_FAIL //количество попыток чтения данных
        def googlePage = []
        while (countFail != 0) {

            try {
                http.request(GET, JSON) {
                    uri.path = Config.NEAR_BY_SEARCH_URI //uri near places
                    //headers.Accept = 'application/json'

                    def keyMap = [location: request.location.latitude + ',' + request.location.longitude,
                                  radius  : request.radius,
                                  key     : Config.KEY,
                                  language: Config.LANGUAGE,
                                 ]
                    if (next_page_token != null) {
                        println next_page_token
                        keyMap += [pagetoken:next_page_token]

                    }

                    //сохраняем url ключи для запроса
                    uri.query = keyMap

                    response.success = { resp, json ->
                        assert resp.status == 200

                        //quary OK
                        if (json.status == StatusCodeEnum.OK as String) {
                            countFail = 0 //если данные прочитаны успешно, то на выход
                        }

                        //try again get quary
                        if (json.status == StatusCodeEnum.UNKNOWN_ERROR as String || json.status == StatusCodeEnum.OVER_QUERY_LIMIT as String) {
                            if (countFail != Config.MAX_FAIL) {
                                sleep(Config.PAUSE)
                                //если флаги: UNKNOWN_ERROR или OVER_QUERY_LIMIT-пытаемся прочитать данные
                                countFail--
                            }
                            throw new GoogleException(json.status as String)
                        }

                        //bad quary
                        if (json.status == StatusCodeEnum.INVALID_REQUEST as String || json.status == StatusCodeEnum.REQUEST_DENIED as String || json.status == StatusCodeEnum.ZERO_RESULTS as String) {
                            //если флаги: INVALID_REQUEST или REQUEST_DENIED-разу на выход
                            println "xx"
                            throw new GoogleException(json.status as String)
                        }

                        print json.next_page_token
                        googlePage << json
                    }
                    response.'404' = { resp ->
                        //do nothing
                    }
                }
            } catch (UnknownHostException ex) {
                throw new GoogleException(ex.message)
            } catch (ConnectException ex) {
                throw new GoogleException(ex.message)
            }
        }
        return googlePage
    }

//Получаем дополнительные данные объекта
    def requestDetailsPage(placeId) throws GoogleException {
        def detail = new DetailPlace()
        try {
            http.request(GET, JSON) {
                uri.path = Config.DETAILS_URI //uri place detail
                uri.query = [placeid : placeId,
                             key     : Config.KEY,
                             language: Config.LANGUAGE]

                response.success = { resp, json ->
                    assert resp.status == 200
                    if (json.status in [StatusCodeEnum.INVALID_REQUEST,
                                        StatusCodeEnum.OVER_QUERY_LIMIT,
                                        StatusCodeEnum.INVALID_REQUEST,
                                        StatusCodeEnum.REQUEST_DENIED]) {
                        throw new GoogleException(json.status)
                    }
                    detail.rating = json.result.rating
                    detail.icon = json.result.icon
                }

                response.'404' = { resp ->
                    println 'Not found'
                }
            }
            return detail
        }
        catch (UnknownHostException ex) {
            throw new GoogleException(ex)
        } catch (ConnectException ex) {
            throw new GoogleException(ex)
        }
    }
}
