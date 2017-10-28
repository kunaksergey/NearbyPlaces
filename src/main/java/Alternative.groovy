import groovyx.net.http.HTTPBuilder
import ua.place.GooglePlace
import ua.place.Place

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

def baseUrl = 'https://maps.googleapis.com'
def nearBySearchUri = '/maps/api/place/nearbysearch/json'
def detailsUri = '/maps/api/place/details/json'
def radius = 500
def key = "AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg"
def language = "en"
def pause = 2000
def BigDecimal latitude = 48.45925
def BigDecimal longitude = 35.04497

def lastRequestTimestamp = 0
def hasRemoutePage = true
def pages = [] //список страниц json
def currentIndex = -1

//Считываем следующую страницу
def next = {
    if (currentIndex < pages.size() - 1) { //Есть ли последующие записи
        currentIndex++
    } else {
        if (!hasRemoutePage) {
            return
        }

        def p = pause - (System.currentTimeMillis() - lastRequestTimestamp)
        if (p > 0) {
            sleep(p as long)//засыпаем если между запросами меньше PAUSE
        }

        def http = new HTTPBuilder(baseUrl)
        http.request(GET, JSON) {
            uri.path = nearBySearchUri //uri near places
            def keyMap = [key     : key,
                          location: latitude + ',' + longitude,
                          radius  : radius,
                          language: language]
            if (pages.size() > 0 && pages[pages.size() - 1].next_page_token != null) {
                keyMap << [pagetoken: pages[pages.size() - 1].next_page_token as String]
            }
            uri.query = keyMap

            response.success = { resp, json ->
                assert resp.status == 200

                pages << json
                currentIndex++
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
}

//прерывание скрипта
def interrupt={message->
    println message
    System.exit(0)
}
//расчет дистанции
def distance = { ltd, lgt ->
    Math.sqrt(Math.pow((Math.abs(latitude) - Math.abs(ltd as BigDecimal)), 2) + Math.pow((Math.abs(longitude) - Math.abs(lgt as BigDecimal)), 2))
}
/** ***********************/

//Парсим JSON
def parsePlace = { it ->
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

//Получить все страницы
def searchAll = {
    def result = []
    pages.each {
        if (it.status != 'OK') interrupt(it.status)
        it.results.each { result << parsePlace(it) }
    }
    result
}
/** *************************/

//Получить текущую страницу
def searchOne = {
    def result = []
    if (pages[currentIndex].status != 'OK') interrupt(pages[currentIndex].status)
    pages[currentIndex].results.each { result << parsePlace(it) }
    result
}
/** *************************/

//Получение предыдущей страницы
def previos = {
    if (currentIndex > 0) {
        currentIndex--
    }
}
/** *************************/

//Компаратор для сравнения полей
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
/** ***********************/

//сортировка по полю
def sortedByField = { result, field ->
    result.sort(comparator.curry(field))
    result
}
/** ***********************/

//Фильтрация по типу
def filterByType = { list, field ->
    def result = []
    list.each {
        if (field == it.types.find { it == field }) {
            result << it
        }
    }
    result
}
/** ************************/

//Ограничение результата
def limit = { list, count ->
    def result = []
    (count > list.size()) ?: list.size()
    result = list.take(count)
    result
}
/** ************************/

//Получаем дополнительные данные объекта
def loadDetails = { place ->
    def http = new HTTPBuilder(baseUrl)

    http.request(GET, JSON) {
        uri.path = detailsUri //uri place detail
        uri.query = [placeid : place.placeId,
                     key     : key,
                     language: language]

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



next()
next()
def reached = searchAll()
def sorted = sortedByField(reached, "distance")
def result = limit(sorted, 10)
assert result[0] instanceof Place
GooglePlace.loadDetails(result[0] as Place)
println result[0]
