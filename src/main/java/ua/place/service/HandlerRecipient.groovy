package ua.place.service

import ua.place.config.Config
import ua.place.entity.Place
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class HandlerRecipient {

    //сортировка по полю
    def sortedByField(sortedBy, listForSorted) {
        assert listForSorted instanceof List

        def list=listForSorted.collect()
        if (sortedBy == null) {
            return list
        }

        try {
            if (!(sortedBy in Place.declaredFields*.name)) {
                throw new NotFieldException(Config.NOT_SORT_FIELD_MESSAGE+":"+sortedBy)
            }
        } catch (NotFieldException e) {
           println e.message
           return list
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

        if (list != 0) {
            list.sort(comparator.curry(sortedBy))
        }
    }

    //Фильтрация по типу
    def filterByType(filter, listForFilter) throws NotTypeException {
        assert listForFilter instanceof List
        if (filter == null) {
            return listForFilter.collect()
        }

        try {
            if (!(filter in Config.types)) {
                throw new NotTypeException(Config.NOT_TYPE_FIELD_MESSAGE+":"+filter)
            }
        }catch (NotTypeException e){
            println e.message
            return listForFilter.collect()
        }

        List list = []
        listForFilter.each {
            if (filter == it.types.find { it == filter }) {
                list << it
            }
        }
        return list
    }
}
