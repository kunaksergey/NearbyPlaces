package ua.place.model.places.component.http

import groovyx.net.http.HTTPBuilder
import ua.place.config.Config
import ua.place.model.places.component.UriDecoderGooglePlaces
import ua.place.model.places.entity.DetailPlace
import ua.place.model.places.enumer.StatusCodeEnum
import ua.place.model.places.exception.GoogleException

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45,38&radius=500&key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

class GooglePagesClient {
    // final static Logger logger = Logger.getLogger(GooglePagesClient.class)

    def uriDecoder = new UriDecoderGooglePlaces()

    def pages(url) throws GoogleException {
        def http = new HTTPBuilder(Config.BASE_URL)
        def countFail = Config.MAX_FAIL //количество попыток чтения данных
        def googlePages = []
        def lastRequestTimestamp = 0
        def prevToken = uriDecoder.getToken(url)
        def currentToken = uriDecoder.getToken(url)
        while (countFail != 0) {
            try {
                def p = Config.PAUSE - (System.currentTimeMillis() - lastRequestTimestamp)
                sleep(p)
                http.request(GET, JSON) {
                    uri.path = Config.NEAR_BY_SEARCH_URI //uri near places
                    //headers.Accept = 'application/json'


                    def keyMap = [location : uriDecoder.getLatitude((url)) + ',' + uriDecoder.getLontitude(url),
                                  radius   : uriDecoder.getRadius(url),
                                  key      : Config.KEY,
                                  language : Config.LANGUAGE,
                                  pagetoken: currentToken
                    ]
                    //сохраняем url ключи для запроса
                    uri.query = keyMap

                    response.success = { resp, json ->
                        assert resp.status == 200

                        //transport OK
                        if (json.status == StatusCodeEnum.OK as String) {
                            //если данные прочитаны успешно, то на выход
                            lastRequestTimestamp = System.currentTimeMillis()
                            googlePages << json
                            currentToken = json.next_page_token
                        }

                        //try again get transport
                        if (json.status == StatusCodeEnum.UNKNOWN_ERROR as String || json.status == StatusCodeEnum.OVER_QUERY_LIMIT as String) {
                            if (countFail != Config.MAX_FAIL) {
                                sleep(Config.PAUSE)
                                //если флаги: UNKNOWN_ERROR или OVER_QUERY_LIMIT-пытаемся прочитать данные
                                countFail--
                            }
                            throw new GoogleException(json.status as String)
                        }

                        //bad transport
                        if (json.status == StatusCodeEnum.INVALID_REQUEST as String || json.status == StatusCodeEnum.REQUEST_DENIED as String || json.status == StatusCodeEnum.ZERO_RESULTS as String) {
                            //если флаги: INVALID_REQUEST или REQUEST_DENIED-разу на выход
                            throw new GoogleException(json.status as String)
                        }

                        return googlePages
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

            if (!(prevToken && currentToken)) countFail=0
            prevToken = currentToken
        }
        return googlePages
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
