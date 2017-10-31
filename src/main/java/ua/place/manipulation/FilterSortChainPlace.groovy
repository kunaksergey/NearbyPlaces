package ua.place.manipulation

import ua.place.config.Config
import ua.place.entity.Place
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class FilterSortChainPlace {
    List places

    def filter(filters) {
        assert filters instanceof List

        def filtersClone = filters.collect()

        filtersClone.retainAll(Config.types)

        if (filtersClone.size() == 0) {
            throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
        }

        def list = places.find {
            def cloneTypes = it.types.collect()
            cloneTypes.retainAll(filtersClone)
            cloneTypes.size() > 0
        }.collect()

        return new FilterSortChainPlace(list)
    }

    def sort(sortBy) {
        if (sortBy == null || !(sortedBy in Place.declaredFields*.name)) {
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

        def list = places.collect()
        list.sort(comparator.curry(sortBy))
        return new FilterSortChainPlace(places: list)
    }

}
