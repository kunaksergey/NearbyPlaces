package ua.place.manipulation

import ua.place.config.Config
import ua.place.enumer.StatusCodeEnum
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

        def list = places.find{
            def cloneTypes = it.types.collect()
            cloneTypes.retainAll(filtersClone)
            cloneTypes.size() > 0
        }.collect()


        return new FilterSortChainPlace(list)
    }

    def sort(fileld) {

    }

}
