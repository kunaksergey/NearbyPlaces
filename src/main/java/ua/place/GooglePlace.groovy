package ua.place
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg&radius=500&location=48.45925,35.04497
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg&radius=500&location=48.45925,35.04497

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.Method.GET
import static groovyx.net.http.ContentType.JSON

class GooglePlace {
    private final static MAX_PLACES_COUNT = 60
    private final static DEFAULT_PLACES_COUNT = 20
    private final static KEY = "AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg"
    private Integer limit = DEFAULT_PLACES_COUNT
    private final static LANGUAGE = "en"
    private final static PAUSE = 1500
    private Integer radius
    private final static String baseUrl = 'https://maps.googleapis.com'
    private final static String nearBySearchUri = '/maps/api/place/nearbysearch/json'
    private final static String detailsUri = '/maps/api/place/details/json'
    private BigDecimal latitude
    private BigDecimal longitude
    def places
    def pages
    def currentPage
    def currentIndex=-1
    def result

    def comparator = { attribute, a, b ->
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


    def findPlaces() {
        loadPlaces()
    }

    def findPlaceSortedByField(field) {
        places ?: load(countSheets(limit))
        places.sort(comparator.curry(field))
    }
    //Если список есть,то возвращаем, иначе делаем запрос
    private def loadPlaces() {
        places ?: load(countSheets(limit))
    }



    //Считываем нужное кол-во страниц
    private def load(countPages) {
        def result = []
        def pageToken

        for (int i = 1; i <= countPages; i++) {
            if (i != 1) {
                sleep(PAUSE)
            }
            def http = new HTTPBuilder(baseUrl)
            http.request(GET, JSON) {
                uri.path = nearBySearchUri //uri near places

                uri.query = [key     : KEY,
                             location: latitude + ',' + longitude,
                             radius  : radius,
                             language: LANGUAGE]
                if (pageToken != null) {
                    uri.query = [key      : KEY,
                                 location : latitude + ',' + longitude,
                                 radius   : radius,
                                 language : LANGUAGE,
                                 pagetoken: pageToken]
                }

                response.success = { resp, json ->
                    assert resp.status == 200
//                    println "My response handler got response: ${resp.statusLine}"
//                    println "Response length: ${resp.headers.'Content-Length'}"
                    pageToken = json.next_page_token

                    json.results.each {
                        def lat=it.geometry.location.lat
                        def lng=it.geometry.location.lng
                        result << new Place(id: it.id, name: it.name, placeId:it.place_id,vicinity: it.vicinity, distance:distance(lat,lng), latitude: lat, longitude: lng,types: it.types)
                    }
                }

                // called only for a 404 (not found) status code:
                response.'404' = { resp ->
                    println 'Not found'
                }
            }

            if (pageToken == null) {
                break;
            }

        }

        places = result
    }



   //Получаем дополнительные данные по id
    static def loadDetail(Place place){

        def http = new HTTPBuilder(baseUrl)

        http.request(GET, JSON) {
            uri.path = detailsUri //uri place detail
            uri.query = [placeid: place.placeId,
                         key   : KEY,
                         language: LANGUAGE]


            response.success = { resp, json ->
                assert resp.status == 200
                place.detail.rating=json.result.rating
                place.detail.icon=json.result.icon
                }

            // called only for a 404 (not found) status code:
            response.'404' = { resp ->
                println 'Not found'
            }
        }

    }

//Получаем дополнительные данные объекта
    void loadDetailPlace(Place place) {
        loadDetail(place.id)
    }

//кол-во запрашиваемых страниц
    private static Integer countSheets(limit) {
        (int) Math.ceil(Math.min(limit, MAX_PLACES_COUNT) / DEFAULT_PLACES_COUNT)
    }

    //кол-во запрашиваемых страниц
    private def distance(ltd,lgt) {
        Math.sqrt(Math.pow((Math.abs(latitude)-Math.abs(ltd)),2)+Math.pow((Math.abs(longitude)-Math.abs(lgt)),2))
    }

    def previos(){
        if(currentIndex>=0){
            currentPage=pages[currentIndex--]
        }
        this
    }

    def searchCurrentPage(){
        def list=[]
        result=currentPage.results.each{list<<parsePlace(it)}
        result=list
        this
    }

    def searchAllPages(){
        pages.each
        this
    }

    def loadResult(){
        result
    }
    def parsePlace(it){

    }
}



