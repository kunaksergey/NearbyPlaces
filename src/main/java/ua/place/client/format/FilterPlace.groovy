package ua.place.client.format

import ua.place.client.enumer.StatusCodeEnum
import ua.place.client.exception.NotTypeException
import ua.place.server.config.Config

/**
 * Created by sa on 02.11.17.
 */
class FilterPlace {
    def filterBy
    def filter(places) throws NotTypeException {
        assert filterBy instanceof List
        def filtersClone = filterBy.collect()
        filtersClone.retainAll(Config.types)
          if (filtersClone.size() == (0 as int)) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING)
            }
            def filteredPlaces = places.find {
                def cloneTypes = it.types.collect()
                cloneTypes.retainAll(filtersClone)
                cloneTypes.size() > 0
            }.collect()
            return filteredPlaces
        }
    }

