package ua.place.client.format

import ua.place.client.exception.NotFieldException
import ua.place.client.exception.NotTypeException
import ua.place.client.format.FilterPlace
import ua.place.client.format.SortPlace

/**
 * Created by sa on 02.11.17.
 */
class FormatProcessor {

    def filterBy(places, filterBy) throws NotTypeException {
        return new FilterPlace(filterBy: filterBy).filter(places)
    }

    def sortedBy(places, sortedBy) throws NotFieldException {
        return new SortPlace(sortedBy: sortedBy).sort(places)
    }

}
