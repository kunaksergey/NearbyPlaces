package ua.place.service

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import ua.place.config.Config
import ua.place.entity.InсomeData
import ua.place.enumer.StatusCodeEnum

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

class GooglePageRecipient {
    def http = new HTTPBuilder(Config.BASE_URL)

    def requestPages(incomeData) {
        assert incomeData instanceof InсomeData
        def countFail = 0
        def pages = []
        def hasRemoutePage = true
        def lastRequestTimestamp = System.currentTimeMillis()

        try {
            for (int i = 0; i < limitPage(incomeData.limitPages); i++) {

                if (!hasRemoutePage || countFail == Config.MAX_FAIL) {
                    return
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
                                println json.status
                            }
                        }

                        //bad data
                        if (json.status == StatusCodeEnum.INVALID_REQUEST as String || json.status == StatusCodeEnum.REQUEST_DENIED as String) {
                            countFail = Config.MAX_FAIL
                            println json.status
                        }

                        //data OK
                        if (json.status == StatusCodeEnum.OK as String) {
                            pages << json
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
        } catch (UnknownHostException e) {
            println e.message
        } catch (ConnectException e) {
            println e.stackTrace
        } finally {
            return pages
        }
    }

    private def limitPage(limit) {
        (limit < 1 || limit > 3) ? Config.MAX_PAGES_GOOGLE_API : limit
    }

//Получаем дополнительные данные объекта
    def requestDetailsPage() {
        http.request(GET, JSON) {
            uri.path = Config.DETAILS_URI //uri place detail
            uri.query = [placeid : userData.place.placeId,
                         key     : Config.KEY,
                         language: Config.LANGUAGE]

            response.success = { resp, json ->
                assert resp.status == 200
                place.detail.rating = json.result.rating
                place.detail.icon = json.result.icon
            }

            response.'404' = { resp ->
                println 'Not found'
            }
        }
    }
}
