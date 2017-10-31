package ua.place.service

import ua.place.config.Config
import ua.place.entity.Place
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException

class HandlerRecipient {

    //сортировка по полю
    def sortedByField(sortedBy, listForSorted) throws NotFieldException {
        assert listForSorted instanceof List


            if (sortedBy == null || !(sortedBy in Place.declaredFields*.name)) {
                throw new NotFieldException(StatusCodeEnum.NOT_MATCHING)
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

          def list = listForSorted.collect()
            if (list != 0) {
                list.sort(comparator.curry(sortedBy))
            }
             return list
//        } catch (NotFieldException e) {
//            println e.message
//
//        } finally {
//            return list
//        }
    }

    //Фильтрация по типу
    def filterByType(filter, listForFilter)throws NotTypeException {
        assert listForFilter instanceof List
        def list = []
        //try {
            if (filter == null || !(filter in Config.types)) {
                throw new NotTypeException(StatusCodeEnum.NOT_MATCHING)
            }

            listForFilter.each {
                if (filter == it.types.find { it == filter }) {
                    list << it
                }
            }
            return list
//        } catch (NotTypeException e) {
//            println e.message
//            return listForFilter.collect()
//        }

    }
}
