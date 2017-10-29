import groovyx.net.http.HTTPBuilder
import ua.place.Place

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET

baseUrl = 'https://maps.googleapis.com'
nearBySearchUri = '/maps/api/place/nearbysearch/json'
detailsUri = '/maps/api/place/details/json'
radius = 500
key = "AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg"
language = "en"
pause = 2000
BigDecimal latitude = 48.45925
BigDecimal longitude = 35.04497

lastRequestTimestamp = 0
hasRemoutePage = true
pages = [] //список страниц json
currentIndex = -1
http = new HTTPBuilder(baseUrl)

//Считываем следующую страницу
request = {
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

//Получаем дополнительные данные объекта
requestDetails = { place ->
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

//прерывание скрипта
interrupt = { message ->
    println message
    System.exit(0)
}
//расчет дистанции
distance = { ltd, lgt ->
    Math.sqrt(Math.pow((Math.abs(latitude) - Math.abs(ltd as BigDecimal)), 2) + Math.pow((Math.abs(longitude) - Math.abs(lgt as BigDecimal)), 2))
}
/** ***********************/

//Парсим JSON
parsePlace = { it ->
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
searchAll = {
    def result = []
    pages.each {
        if (it.status != 'OK') interrupt(it.status)
        it.results.each { result << parsePlace(it) }
    }
    result
}
/** *************************/

//Получить текущую страницу
searchOne = {
    def result = []
    if (pages[currentIndex].status != 'OK') interrupt(pages[currentIndex].status)
    pages[currentIndex].results.each { result << parsePlace(it) }
    result
}
/** *************************/

//Получение предыдущей страницы
previos = {
    if (currentIndex > 0) {
        currentIndex--
    }
}
/** *************************/

//Компаратор для сравнения полей
comparator = { attribute, a, b ->
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
sortedByField = { result, field ->
    result.sort(comparator.curry(field))
    result
}
/** ***********************/

//Фильтрация по типу
filterByType = { list, field ->
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
limit = { list, count ->
    (count > list.size()) ?: list.size()
    result = list.take(count)
}
/** ************************/

//Получаем дополнительные данные объекта
loadDetails = { place ->
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



request()
request()
reached = searchAll()
sorted = sortedByField(reached, "distance")
result = limit(sorted, 10)
assert result[0] instanceof Place
requestDetails(result[0] as Place)
println result[0]
