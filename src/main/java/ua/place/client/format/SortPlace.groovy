package ua.place.client.format

import ua.place.client.enumer.StatusCodeEnum
import ua.place.client.exception.NotFieldException
import ua.place.entity.place.Place

/**
 * Created by sa on 02.11.17.
 */
//Сортировщик
class SortPlace {
    def sortedBy

    def sort(places) throws NotFieldException {

        def placesClone = places.collect()
        if (sortedBy == '') {
            return placesClone
        }
        if (!(sortedBy in Place.declaredFields*.name)) {
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

        placesClone.sort(comparator.curry(sortedBy))
        return (placesClone)

    }
}