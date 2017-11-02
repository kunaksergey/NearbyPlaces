package ua.place.client.process

import ua.place.client.exception.NotFieldException
import ua.place.client.exception.NotTypeException
import ua.place.client.factory.FormatProcessor
import ua.place.client.factory.QuaryCreator
import ua.place.client.tools.PrintData
import ua.place.client.ui.ConsoleDialog
import ua.place.client.validator.AnswerValidator
import ua.place.entity.quary.Request
import ua.place.entity.quary.Response
import ua.place.server.process.ServerProcess
import ua.place.server.validator.RequestValidator

class ClientProcess {
    def quaryCreator = new QuaryCreator() //обработчик запросов
    def consoleDialog = new ConsoleDialog() //работа с консолью
    def formatProcessor = new FormatProcessor() //процессор сортировки и фильтрации
    def printer = new PrintData() //печать результатов

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

    def dialog() {
        consoleDialog.dialog()
    }
    //первый запрос
    def createRequest(answerMap) {
        def query = quaryCreator.createQuary(answerMap)
        return query
    }

    //второй запрос
    def createRequest(request, responce) {
        return quaryCreator.createQuary(request, responce)
    }

    def yesOrNot(def message) {
        consoleDialog.yesOrNot(message)
    }

    //фильтрация и сортировка places
    private def handlePlaces(places) {
        printer.printPlaces(filterPlaces(places))
        printer.printPlaces(sortPlaces(places))
    }

    //возвращаем отфильтрованный список places
    private def filterPlaces(places) {
        try {
            //если нет фильтров не фильтруем
            def filterBy = consoleDialog.getFilter() as List
            if (filterBy.size() == 0 as int) {
                return []
            }
            return formatProcessor.filterBy(places, filterBy)
        } catch (NotTypeException ex) {
            println ex.message
            return []
        }
    }

    //возвращаем отсортированный список places
    private def sortPlaces(places) {
        try {
            def sortedBy = consoleDialog.sortDialog()
            //если не ввели поле не сортируем
            if (sortedBy.length() == (0 as int)) {
                return places
            }
            return formatProcessor.sortedBy(places, sortedBy)
        } catch (NotFieldException ex) {
            println ex.message
            return places
        }
    }

    def getResponse(Request request) {
        new ServerProcess().getResponse(request)//SERVER
    }

    boolean hasNextData(Response response) {
        response.status == '[OK]' &&
                response.next_page_token != null
    }

    def shortDialog() {
        consoleDialog.shortDialog()
    }

    def validate(answerMap) {
        new AnswerValidator().validate(answerMap)
    }
}
