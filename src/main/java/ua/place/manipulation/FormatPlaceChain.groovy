package ua.place.manipulation

import ua.place.config.Config
import ua.place.entity.place.Place
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class FormatPlaceChain {
    List places

    def filter(filters) {
        try {
            if (filters == null) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
            }

            def filtersClone = filters.collect()
            filtersClone.retainAll(Config.types)

            if (filtersClone.size()==0) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
            }
        } catch (NotTypeException ex) {
            return new FormatPlaceChain(places: places.collect())
        }

        def filteredPlaces = places.find {
            def cloneTypes = it.types.collect()
            cloneTypes.retainAll(filtersClone)
            cloneTypes.size() > 0
        }.collect()

        return new FormatPlaceChain(places: filteredPlaces)
    }

    def sort(sortBy) {
        try {
            if (sortBy == null || !(sortBy in Place.declaredFields*.name)) {
                throw new NotFieldException(StatusCodeEnum.NOT_MATCHING as String)
            }
        } catch (NotFieldException ex) {
            return new FormatPlaceChain(places: places.collect())
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

        def placesClone = places.collect()
        placesClone.sort(comparator.curry(sortBy))
        return new FormatPlaceChain(places: placesClone)
    }
}


