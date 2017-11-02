package ua.place.client.format

import ua.place.client.enumer.StatusCodeEnum
import ua.place.client.exception.NotTypeException


/**
 * Created by sa on 02.11.17.
 */
class FilterPlace {
    def filterBy
    def filter(places) throws NotTypeException {
        assert filterBy instanceof List
         def filteredPlaces = places.findAll{
             def filterByClone=filterBy.collect()
                def cloneTypesPlace = it.types.collect()
                filterByClone.removeAll(cloneTypesPlace)
                filterByClone.size()!=filterBy.size()
            }
        if(filteredPlaces.size()==0 as int) throw new NotTypeException(StatusCodeEnum.NOT_MATCHING as String)
            return filteredPlaces
        }
    }


