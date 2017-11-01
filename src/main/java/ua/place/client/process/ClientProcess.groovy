package ua.place.client.process

import ua.place.entity.data.IncomeData
import ua.place.entity.data.OutcomeData
import ua.place.client.factory.IncomeDataCreator
import ua.place.client.format.FilterAndSortPlaceChain
import ua.place.client.tools.PrintData
import ua.place.client.ui.ConsoleDialog

class ClientProcess {
    def incomeDataCreator = new IncomeDataCreator()
    def consoleDialog = new ConsoleDialog()
    def printer = new PrintData()

    def printIncomeData(incomeDate) {
        printer.printIncomeData(changeIncomeData())
    }

    def printOutcomeData(outcomeData) {
        printer.printIncomeData(outcomeData)
    }

    def formatData(incomeData, outcomeData) {
        assert incomeData instanceof IncomeData
        assert outcomeData instanceof OutcomeData

        def outcomeDataClone = outcomeData.clone() //клонируем серверный ответ

        new FilterAndSortPlaceChain(places: outcomeData.places).
                filter(incomeData.filterBy).
                sort(incomeData.sortedBy).
                places

        outcomeDataClone.places = formatedPlaces //сохраняем в клоне форматированные plac'ы
        return outcomeDataClone
    }

    def changeIncomeData() {
        def answerMap = consoleDialog.dialog()
        return incomeDataCreator.createIncomeData(answerMap)
    }

    def changeIncomeData(incomeData) {
        assert incomeData instanceof IncomeData
        def incomeDataClone = incomeData.clone() as IncomeData
        def answersMap = [:]
        answersMap << consoleDialog.shotDialog()
        incomeDataClone.filterBy = answersMap['filterBy']//?
        incomeDataClone.sortedBy = answersMap['sortedBy']
        return incomeDataClone
    }

    def changeIncomeData(incomeData,outcomeData) {
        assert incomeData instanceof IncomeData
        assert outcomeData instanceof OutcomeData
        def incomeDataClone = incomeData.clone() as IncomeData
        incomeData.next_page_token=outcomeData.token
        return incomeDataClone
    }

    def reply(def message) {
        consoleDialog.reply(message)
    }

}
