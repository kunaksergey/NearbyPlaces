package ua.place.format

import ua.place.config.Config
import ua.place.entity.place.Place
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class FilterAndSortPlaces {
    List places

     def filter(filters) {
        try {
            if (filters == null) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
            }

            def filtersClone = filters.collect()
            filtersClone.retainAll(Config.types)

            if (filtersClone.size() == 0) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
            }

            def filteredPlaces = places.find {
                def cloneTypes = it.types.collect()
                cloneTypes.retainAll(filtersClone)
                cloneTypes.size() > 0
            }.collect()
            return new FilterAndSortPlaces(places: filteredPlaces)
        } catch (NotTypeException ex) {
            println 'filtered: '+ex.message
            return new FilterAndSortPlaces(places: places.collect())
        }
    }

    def sort(sortBy) {
        try {
            if (sortBy == null || !(sortBy in Place.declaredFields*.name)) {
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

            def placesClone = places.collect()
            placesClone.sort(comparator.curry(sortBy))
            return new FilterAndSortPlaces(places: placesClone)
        } catch (NotFieldException ex) {
            println 'sorted: '+ex.message
            return new FilterAndSortPlaces(places: places.collect())
        }
    }
}


