package ua.place

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

//https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg&radius=500&location=48.45925,35.04497
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg&radius=500&location=48.45925,35.04497
class GooglePlace {
    private final static String BASE_URL = 'https://maps.googleapis.com'
    private final static String NEAR_BY_SEARCH_URI = '/maps/api/place/nearbysearch/json'
    private final static String DETAILS_URI = '/maps/api/place/details/json'
    private final static DEFAULT_RADIUS = 500
    private final static KEY = "AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg"
    private final static LANGUAGE = "en"
    private final static PAUSE = 2000

    private def radius = DEFAULT_RADIUS
    private BigDecimal latitude
    private BigDecimal longitude
    private lastRequestTimestamp = 0
    private def hasRemoutePage = true
    private def pages = [] //список страниц json
    private def currentIndex = -1
    private def result //результат-list Places

    private def comparator = { attribute, a, b ->
        def aValue = a.getProperty(attribute)
        def bValue = b.getProperty(attribute)

        if (!aValue && !bValue)
            return 0
        else if (!aValue)
            return -1
        else if (!bValue)
            return 1
        else
            return aValue.compareTo(bValue)
    }

    //сортировка по полю
    def sortedByField(field) {
        result.sort(comparator.curry(field))
        this
    }

    //Считываем следующую страницу
    def next() {
        if (currentIndex < pages.size() - 1) { //Есть ли последующие записи
            currentIndex++
        } else {
            if (!hasRemoutePage) {
                return this
            }
            def http = new HTTPBuilder(BASE_URL)
            def p = PAUSE - (System.currentTimeMillis() - lastRequestTimestamp)
            if (p > 0) {
                sleep(p as long)//засыпаем если между запросами меньше PAUSE
            }
            http.request(GET, JSON) {
                uri.path = NEAR_BY_SEARCH_URI //uri near places

                uri.query = [key     : KEY,
                             location: latitude + ',' + longitude,
                             radius  : radius,
                             language: LANGUAGE]
                if (pages.size() > 0 && pages[pages.size() - 1].next_page_token != null) {
                    uri.query = [key      : KEY,
                                 location : latitude + ',' + longitude,
                                 radius   : radius,
                                 language : LANGUAGE,
                                 pagetoken: pages[pages.size() - 1].next_page_token]
                }

                response.success = { resp, json ->
                    assert resp.status == 200

                    pages << json
                    if (json.next_page_token == null) {
                        hasRemoutePage = false
                    }
                }

                // called only for a 404 (not found) status code:
                response.'404' = { resp ->
                    println 'Not found'
                }
            }
            lastRequestTimestamp = System.currentTimeMillis()
        }
        currentIndex++
        this
    }

    def previos() {
        if (currentIndex > 0) {
            currentIndex--
        }
        this
    }

    //Получаем дополнительные данные объекта
    static def loadDetails(Place place) {

        def http = new HTTPBuilder(BASE_URL)

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

            // called only for a 404 (not found) status code:
            response.'404' = { resp ->
                println 'Not found'
            }
        }

    }

    //расчет дистанции
    private def distance(ltd, lgt) {
        Math.sqrt(Math.pow((Math.abs(latitude) - Math.abs(ltd as BigDecimal)), 2) + Math.pow((Math.abs(longitude) - Math.abs(lgt as BigDecimal)), 2))
    }


    def limit(count){
        (count>result.size())?:result.size()
        result=result.take(count)
        this
    }

    def searchCurrentPage() {
        def list = []
        if (currentIndex >= 0 && currentIndex < pages.size()) {
            result = pages[currentIndex].results.each { list << parsePlace(it) }
        }
        result = list
        this
    }

    def searchAllPages() {
        def list = []
        pages.each { it.results.each { list << parsePlace(it) } }
        result = list
        this
    }

    def loadResult() {
        result
    }

    private def parsePlace(it) {
        def lat = it.geometry.location.lat
        def lng = it.geometry.location.lng
        def place = new Place();
        place.id = it.id
        place.name = it.name
        place.placeId = it.place_id
        place.vicinity = it.vicinity
        place.distance = distance(lat, lng)
        place.latitude = lat as BigDecimal
        place.longitude = lng as BigDecimal
        place.types = it.types
        place
    }
}



