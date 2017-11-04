package ua.place.model.places.component

/**
 * Created by sa on 04.11.17.
 */
class FilterAndSort {
    def places
    def filter(filterBy){
       return  new FilterAndSort(places:new FilterPlaces(filterBy:filterBy).filter(places))
    }
   def  sort(sortedBy){
        return new FilterAndSort(places:new SortPlace(sortedBy:sortedBy).sort(places))
    }
}
