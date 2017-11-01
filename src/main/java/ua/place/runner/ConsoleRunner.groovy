package ua.place.runner

import ua.place.ui.ConsoleDialog
import ua.place.process.DataManingProcess
import ua.place.parser.AnswerIncomeDataParser
import ua.place.tools.PrintData

class ConsoleRunner {
    def run() {
        def console = new ConsoleDialog()
        def printerData = new PrintData()
        def replay = true
        while (replay) {
            def procces = new DataManingProcess()
            def incomeDate = console.getIncomeData()
            def outcomeData = procces.getOutcomeData(incomeDate)
            printerData.printIncomeData(incomeDate)
            printerData.printOutcomeData(outcomeData)
            replay = console.reply()
        }
    }
}