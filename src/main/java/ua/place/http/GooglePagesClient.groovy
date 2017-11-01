package ua.place.http

import groovyx.net.http.HTTPBuilder
import ua.place.config.Config
import ua.place.entity.place.DetailPlace
import ua.place.entity.data.IncomeData
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.GoogleException

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

class GooglePagesClient {
    def http = new HTTPBuilder(Config.BASE_URL)

    def requestPages(incomeData) throws GoogleException {

        assert incomeData instanceof IncomeData
        def countFail = 0
        def pages = []
        def hasRemoutePage = true
        def lastRequestTimestamp = System.currentTimeMillis()

        try {
            for (int i = 0; i < limitPage(incomeData.limitPages); i++) {

                if (!hasRemoutePage || countFail == Config.MAX_FAIL) {
                    return pages
                }

                def p = Config.PAUSE - (System.currentTimeMillis() - lastRequestTimestamp)
                if (p > 0) {
                    sleep(p as long)//засыпаем если между запросами меньше PAUSE
                }

                http.request(GET, JSON) { req ->
                    uri.path = Config.NEAR_BY_SEARCH_URI //uri near places

                    def keyMap = [key     : Config.KEY,
                                  location: incomeData.location.latitude + ',' + incomeData.location.longitude,
                                  rankby  : 'distance',
                                  language: Config.LANGUAGE]
                    if (pages.size() > 0 && pages[pages.size() - 1].next_page_token != null) {
                        keyMap << [pagetoken: pages[pages.size() - 1].next_page_token as String]
                    }

                    uri.query = keyMap

                    response.success = { resp, json ->
                        assert resp.status == 200

                        //try again get data
                        if (json.status == StatusCodeEnum.UNKNOWN_ERROR as String || json.status == StatusCodeEnum.OVER_QUERY_LIMIT as String) {
                            if (countFail != Config.MAX_FAIL) {
                                sleep(Config.PAUSE)
                                i--
                                countFail++

                            }
                            throw new GoogleException(json.status)
                        }

                        //bad data
                        if (json.status == StatusCodeEnum.INVALID_REQUEST as String || json.status == StatusCodeEnum.REQUEST_DENIED as String) {
                            countFail = Config.MAX_FAIL
                            throw new GoogleException(json.status)
                            //println json.status
                        }

                        //data OK
                        if (json.status == StatusCodeEnum.OK as String) {
                            pages.addAll(json.results)
                            countFail = 0
                        }

                        if (json.next_page_token == null || json.next_page_token == '0') {
                            hasRemoutePage = false
                        }
                    }
                    response.'404' = { resp ->
                        println 'Not found'
                    }
                }
                lastRequestTimestamp = System.currentTimeMillis()
            }
            pages
        }
        catch (UnknownHostException ex) {
            throw new GoogleException(ex.message)
        } catch (ConnectException e) {
            throw new GoogleException(e.message)
        }
    }

    private def limitPage(limit) {
        (limit < 1 || limit > 3) ? Config.MAX_PAGES_GOOGLE_API : limit
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
            throw new GoogleException(ex.message)
        } catch (ConnectException e) {
            throw new GoogleException(e.message)
        }
    }
}
