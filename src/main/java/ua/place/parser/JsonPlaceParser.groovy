package ua.place.parser

import ua.place.entity.place.Location
import ua.place.entity.place.Place

class JsonPlaceParser {

    def parsePages(inLocation, listPages) {
        assert listPages instanceof List
        //Парсим JSON объект
        def parsePlace = { itPlace ->

            //Расчет дистанции
            def distance = { x, y, x1, y1 ->
                Math.sqrt(

                        Math.pow(x - x1, 2) +
                                Math.pow(y - y1, 2)
                )
            }

            def place = new Place();
            def location = new Location(latitude: itPlace.geometry.location.lat, longitude: itPlace.geometry.location.lng)

            place.location = location
            place.id = itPlace.id
            place.placeId = itPlace.place_id
            place.name = itPlace.name
            place.vicinity = itPlace.vicinity
            place.distance = distance(inLocation.latitude,
                    inLocation.longitude,
                    location.latitude,
                    location.longitude
            )

            place.types = itPlace.types
            return place
        }

        List list = []
        listPages.each {
            list << parsePlace(it)
        }
        return list
    }
}
