package ua.place.client.process

import ua.place.client.exception.NotFieldException
import ua.place.client.exception.NotTypeException
import ua.place.client.format.FormatProcessor
import ua.place.client.factory.QuaryCreator
import ua.place.client.tools.PrintData
import ua.place.client.ui.ConsoleDialog
import ua.place.entity.quary.Response

class ClientProcess {
    def quaryCreator = new QuaryCreator()
    def consoleDialog = new ConsoleDialog()
    def formatProcessor = new FormatProcessor()
    def printer = new PrintData()

    //Обработка пришедшего ответа на клиенте
    def handleResponse(response) {
        assert response instanceof Response
        printer.printResponse(response)
        def replyFormat = true
        //обрабатываем, только если есть коллекция
        if (response.places.size() != (0 as int)) {
            while (replyFormat) {
                handlePlaces(response.places)
                replyFormat = yesOrNot("Reply format?")
            }
        }
    }

    //первый запрос
    def createQuary() {
        def answerMap = consoleDialog.dialog()
        return quaryCreator.createQuary(answerMap)
    }

    //второй запрос
    def createQuary(request, responce) {
        return quaryCreator.createQuary(request, responce)
    }

    def yesOrNot(def message) {
        consoleDialog.reply(message)
    }

    //фильтрация и сортировка plas'ов
    private def handlePlaces(places) {
        printer.printPlaces(filterPlaces(places))
        printer.printPlaces(sortPlaces(places))
    }

    //возвращаем отфильтрованный список places
    private def filterPlaces(places) {
        try {
            //если нет фильтров не фильтруем
            def filterBy = consoleDialog.getFilter()
            if (filterBy.size() == 0 as int) {
                return
            }
            return formatProcessor.filterBy(places, filterBy)
        } catch (NotTypeException ex) {
            println ex.message
            return
        }
    }

    //возвращаем отсортированный список places
    private def sortPlaces(places) {
        try {
            def sortedBy = consoleDialog.sortDialog()
            //если не ввели поле не сортируем
            if (sortedBy.length() == (0 as int)) {
                return
            }
            return formatProcessor.sortedBy(places, sortedBy)
        } catch (NotFieldException ex) {
            println ex.message
            return
        }
    }

}
