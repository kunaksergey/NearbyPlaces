package ua.place.service

import groovy.transform.Immutable
import groovyx.net.http.HTTPBuilder
import ua.place.config.Config
import ua.place.entity.UserData

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

class GooglePlaceRecipient {
    private http = new HTTPBuilder(Config.BASE_URL)
    UserData userData

    def request() {
        def pages = []
        def hasRemoutePage = true
        def lastRequestTimestamp = System.currentTimeMillis()

        for (int i = 0; i < limitPage(userData.incomeData.limitPages); i++) {

            if (!hasRemoutePage) {
                return
            }

            def p = Config.PAUSE - (System.currentTimeMillis() - lastRequestTimestamp)
            if (p > 0) {
                sleep(p as long)//засыпаем если между запросами меньше PAUSE
            }


            http.request(GET, JSON) {
                uri.path = Config.NEAR_BY_SEARCH_URI //uri near places

                def keyMap = [key     : Config.KEY,
                              location: userData.incomeData.location.latitude + ',' + userData.incomeData.location.longitude,
                              rankby  : 'distance',
                              language: Config.LANGUAGE]
                if (pages.size() > 0 && pages[pages.size() - 1].next_page_token != null) {
                    keyMap << [pagetoken: pages[pages.size() - 1].next_page_token as String]
                }

                uri.query = keyMap

                response.success = { resp, json ->
                    assert resp.status == 200
                    pages << json
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
        userData.pages<<pages
    }

    private def limitPage(limit){
        (limit<1||limit>3)?Config.MAX_PAGES_GOOGLE_API:limit
    }
    //Получаем дополнительные данные объекта
    def requestDetails() {
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
