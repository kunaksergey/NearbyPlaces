package ua.place.model.places.component

import ua.place.model.places.enumer.StatusCodeEnum
import ua.place.model.places.exception.NotTypeException


/**
 * Created by sa on 02.11.17.
 */
class FilterPlaces {
    def filterBy
    def filter(places) throws NotTypeException {
        filterBy as List
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


