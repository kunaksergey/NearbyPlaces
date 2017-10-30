package ua.place.service

import ua.place.entity.Location
import ua.place.entity.Place
import ua.place.exception.NotReceivedException

class Parser {
    //Получить все страницы
    def userData

    def parse() throws NotReceivedException {
        List list = []
        userData.pages.each {
            if (it.status != 'OK') throw new NotReceivedException(it.status)
            it.results.each { list << parsePlace(it) }
        }
        userData.listPlace=list
    }

    //Парсим JSON
    private def parsePlace(itPlace) {

        //Расчет дистанции
        def distance = { x,y,x1,y1 ->
            Math.sqrt(

                    Math.pow(x - x1,2) +
                            Math.pow(y - y1,2)
            )
        }

        def place = new Place();
        def location=new Location(latitude:itPlace.geometry.location.lat,longitude:itPlace.geometry.location.lng)
        place.location=location
        place.id = itPlace.id
        place.placeId = itPlace.place_id
        place.name = itPlace.name
        place.vicinity = itPlace.vicinity
        place.distance = distance(userData.incomeData.location.latitude,
                                  userData.incomeData.location.longitude,
                                  location.latitude,
                                  location.longitude
                                  )

        place.types = itPlace.types
        place
    }
}
