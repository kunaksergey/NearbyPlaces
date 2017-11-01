package ua.place.runner

import ua.place.process.DataManingProcess
import ua.place.tools.PrintData
import ua.place.process.ConsoleProcess

class ApplicationRunner {
    def run() {

        def printerData = new PrintData()
        def replayQuary = true
        while (replayQuary) {
            def consoleProcess = new ConsoleProcess()
            def dataManingProcess = new DataManingProcess()
            def incomeDate = consoleProcess.getIncomeData()
            def outcomeData = dataManingProcess.getOutcomeData(incomeDate)
            printerData.printIncomeData(incomeDate)
            printerData.printOutcomeData(outcomeData)
            def replayFormat=consoleProcess.reply("Format replay?")
            while(replayFormat){
                def inData = consoleProcess.getIncomeData(incomeDate)
                def outData=dataManingProcess.getOutcomeData(inData)
                printerData.printIncomeData(inData)
                printerData.printOutcomeData(outData)
                replayFormat=consoleProcess.reply("Format replay?")
            }
            replayQuary = consoleProcess.reply("Google quary replay?")
        }
    }
}