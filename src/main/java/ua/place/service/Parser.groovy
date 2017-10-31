package ua.place.service

import ua.place.entity.Location
import ua.place.entity.Place
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.NotReceivedException

class Parser {
    //входные данные
    def incomeData

    def parsePages(listPages) {
        List list = []
        listPages.each {
            //try{
            if (it.status != StatusCodeEnum.OK as String) throw new NotReceivedException(it.status)
            it.results.each { list << parsePlace(it) }
//            }catch (NotReceivedException e){
//                println StatusCodeEnum.OK
//                return list
//            }
        }
        return list
    }

    //Парсим JSON объект
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
        place.distance = distance(incomeData.location.latitude,
                                  incomeData.location.longitude,
                                  location.latitude,
                                  location.longitude
                                  )

        place.types = itPlace.types
        return place
    }
}
