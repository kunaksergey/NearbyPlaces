package ua.place.service

import ua.place.config.Config
import ua.place.entity.Place
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class HandlerRecipient {

    //сортировка по полю
    def sortedByField(sortedBy, listForSorted) {
        if (sortedBy == null) {
            return listForSorted
        }

        try {
            if (Place.getMetaPropertyValues().hasProperty(userData.incomeData.sortedByField) == null) {
                throw new NotFieldException(Config.NOT_SORT_FIELD_MESSAGE)
            }
        } catch (NotFieldException e) {
            userData.log.listMessage << e.message
            return
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

        if (userData.listPlace.size() != 0) {
            userData.listPlace.sort(comparator.curry(userData.incomeData.sortedByField))
        }
    }

    //Фильтрация по типу
    def filterByType(filter, listForFilter) throws NotTypeException {
        if (filter == null) {
            return listForFilter
        }

        if (!(filter in Config.types)) {
            throw new NotTypeException(Config.NOT_TYPE_MESSAGE)
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
