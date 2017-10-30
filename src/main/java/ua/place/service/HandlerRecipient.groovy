package ua.place.service

import ua.place.config.Config
import ua.place.entity.Place
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class HandlerRecipient {
    def userData

    //сортировка по полю
    def sortedByField() {
        if (userData.incomeData.sortedByField == null) {
            return
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
    def filterByType() {
        if (userData.incomeData.filterBy == null) {
            return
        }

        try {
            if (!(userData.incomeData.filterBy in Config.types)) {
                throw new NotTypeException(Config.NOT_TYPE_MESSAGE)
            }
        }
        catch (NotTypeException e) {
            userData.log.listMessage << e.message
            return
        }

        if (userData.incomeData.filterBy in Config.types) {
            List list = []
            userData.listPlace.each {
                if (userData.incomeData.filterBy == it.types.find { it == userData.incomeData.filterBy }) {
                    list << it
                }
            }
            userData.listPlace = list
        } else {
            userData.log.listMessage << Config.NOT_TYPE_MESSAGE
        }
    }
}
