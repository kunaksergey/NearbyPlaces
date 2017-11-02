package ua.place.client.format

import ua.place.client.enumer.StatusCodeEnum
import ua.place.entity.place.Place
import ua.place.client.exception.NotFieldException


/**
 * Created by sa on 02.11.17.
 */
//Сортировщик
class SortPlace {
    def sortBy

    def sort(places) throws NotFieldException {
        try {
            def placesClone = places.collect()
            if (sortBy == ''){
                return placesClone
            }

            if ((sortBy in Place.declaredFields*.name)) {
                throw new NotFieldException(StatusCodeEnum.NOT_MATCHING as String)
            }

            //Компаратор для сравнения полей
            def comparator = { field, a, b ->
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


            placesClone.sort(comparator.curry(sortBy))
            return (placesClone)
        } catch (NotFieldException ex) {
            return places.collect()
        }
    }
}