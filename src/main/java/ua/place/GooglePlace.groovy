package ua.place

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg&radius=500&location=48.45925,35.04497
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg&radius=500&location=48.45925,35.04497
class GooglePlace {
    private final static BASE_URL = 'https://maps.googleapis.com'
    private final static NEAR_BY_SEARCH_URI = '/maps/api/place/nearbysearch/json'
    private final static DETAILS_URI = '/maps/api/place/details/json'
    private final static DEFAULT_RADIUS = 500
    private final static KEY = "AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg"
    private final static LANGUAGE = "en"
    private final static PAUSE = 2000

    private radius = DEFAULT_RADIUS
    private latitude
    private longitude
    private lastRequestTimestamp = 0
    private hasRemoutePage = true
    private pages = [] //список страниц json
    def result = [] //результат-list Places
    def listFunction = []
    def http = new HTTPBuilder(BASE_URL)
    //Считываем следующую страницу
    def request() {
        listFunction << {
            if (!hasRemoutePage) {
                return this
            }

            def p = PAUSE - (System.currentTimeMillis() - lastRequestTimestamp)
            if (p > 0) {
                sleep(p as long)//засыпаем если между запросами меньше PAUSE
            }


            http.request(GET, JSON) {
                uri.path = NEAR_BY_SEARCH_URI //uri near places

                def keyMap = [key     : KEY,
                              location: latitude + ',' + longitude,
                              radius  : radius,
                              language: LANGUAGE]
                if (pages.size() > 0 && pages[pages.size() - 1].next_page_token != null) {
                    keyMap << [pagetoken: pages[pages.size() - 1].next_page_token as String]
                }

                uri.query = keyMap

                response.success = { resp, json ->
                    assert resp.status == 200
                    pages << json
                    if (json.next_page_token == null) {
                        hasRemoutePage = false
                    }
                }

                response.'404' = { resp ->
                    println 'Not found'
                }
            }
            lastRequestTimestamp = System.currentTimeMillis()
            processAll()
        }
        this
    }

    //Получить все страницы
    private def processAll() throws NotReceivedException {
        List list = []
        pages.each {
            if (it.status != 'OK') throw new NotReceivedException(it.status)
            it.results.each { list << parsePlace(it) }
        }
        result = list

        this
    }

    //сортировка по полю
    def sortedByField(field) {
        listFunction << {
            //Компаратор для сравнения полей
            def comparator = { a, b ->
                def aValue = a.getProperty(field)
                def bValue = b.getProperty(field)

                if (!aValue && !bValue)
                    return 0
                else if (!aValue)
                    return -1
                else if (!bValue)
                    return 1
                else
                    return aValue.compareTo(bValue)
            }
            if(result.size()!=0){
            result.sort(comparator)
            }
        }
        this
    }

    //Фильтрация по типу
    def filterByType(field) {
        listFunction << {
            List list = []
            result.each {
                if (field == it.types.find { it == field }) {
                    list << it
                }
            }
            result = list
        }
        this
    }

    //Ограничение результата
    def limit(count) {
        listFunction << {

            (count > result.size()) ?: result.size()
            result = result.take(count)
        }
        this
    }

    //Возвращаем результат
    def loadResult() {
        listFunction.each { it() }
        result
    }

    //Парсим JSON
    def parsePlace(itPlace) {
        //Расчет дистанции
        def distance = { ltd, lgt ->
            Math.sqrt(Math.pow((Math.abs(latitude as BigDecimal) - Math.abs(ltd as BigDecimal)), 2) + Math.pow((Math.abs(longitude as BigDecimal) - Math.abs(lgt as BigDecimal)), 2))
        }
        def lat = itPlace.geometry.location.lat
        def lng = itPlace.geometry.location.lng
        def place = new Place();
        place.id = itPlace.id
        place.name = itPlace.name
        place.placeId = itPlace.place_id
        place.vicinity = itPlace.vicinity
        place.distance = distance(lat, lng)
        place.latitude = lat as BigDecimal
        place.longitude = lng as BigDecimal
        place.types = itPlace.types
        place
    }

    //Получаем дополнительные данные объекта
    def requestDetails(Place place) {
        http.request(GET, JSON) {
            uri.path = DETAILS_URI //uri place detail
            uri.query = [placeid : place.placeId,
                         key     : KEY,
                         language: LANGUAGE]

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



