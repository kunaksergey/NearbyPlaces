package ua.place.runner

import ua.place.ui.ConsoleDialog
import ua.place.process.DataManingProcess
import ua.place.parser.AnswerIncomeDataParser
import ua.place.tools.PrintData

class ConsoleRunner {
    def run() {
        def console = new ConsoleDialog()
        def printerData = new PrintData()
        def replayQuary = true
        while (replayQuary) {
            def procces = new DataManingProcess()
            def incomeDate = console.getIncomeData()
            def outcomeData = procces.getOutcomeData(incomeDate)
            printerData.printIncomeData(incomeDate)
            printerData.printOutcomeData(outcomeData)
            def replayFormat=console.reply("Format replay?")
            while(replayFormat){
                def inData = console.getIncomeData(incomeDate)
                def outData=procces.getOutcomeData(inData)
                printerData.printIncomeData(inData)
                printerData.printOutcomeData(outData)
                replayFormat=console.reply("Format replay?")
            }
            replayQuary = console.reply("Google quary replay?")
        }
    }
}