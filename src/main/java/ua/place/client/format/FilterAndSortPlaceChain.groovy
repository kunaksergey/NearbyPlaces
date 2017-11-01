package ua.place.client.format

import org.apache.log4j.Logger
import ua.place.server.config.Config
import ua.place.entity.place.Place
import ua.place.server.enumer.StatusCodeEnum
import ua.place.server.exception.NotFieldException
import ua.place.server.exception.NotTypeException

class FilterAndSortPlaceChain {
    final static Logger logger = Logger.getLogger(FilterAndSortPlaceChain.class)
    List places

     def filter(filters) {
        try {
            if (filters==null) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
            }

            def filtersClone = filters.collect()
            filtersClone.retainAll(Config.types)

            if (filtersClone.size() == 0) {
                new FilterAndSortPlaceChain(places: [])
            }

            def filteredPlaces = places.find {
                def cloneTypes = it.types.collect()
                cloneTypes.retainAll(filtersClone)
                cloneTypes.size() > 0
            }.collect()
            return new FilterAndSortPlaceChain(places: filteredPlaces)
        } catch (NotTypeException ex) {
            logger.info('filtered: '+ex.message)
            return new FilterAndSortPlaceChain(places: places.collect())
        }
    }

    def sort(sortBy) {
        try {
            if (sortBy == '' || !(sortBy in Place.declaredFields*.name)) {
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
            return new FilterAndSortPlaceChain(places: placesClone)
        } catch (NotFieldException ex) {
            logger.info('sorted: '+ex.message)
            return new FilterAndSortPlaceChain(places: places.collect())
        }
    }
}


