package ua.place.model.places.component

/**
 * Created by sa on 04.11.17.
 */
class FormatAndSort {
    def places
    def filter(filterBy){
       return  new FormatAndSort(places:new FilterPlaces(filterBy:filterBy).filter(places))
    }
   def  sort(sortedBy){
        return new FormatAndSort(places:new SortPlace(sortedBy:sortedBy).sort(places))
    }
}
