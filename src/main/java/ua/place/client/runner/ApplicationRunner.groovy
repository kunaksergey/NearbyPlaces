package ua.place.client.runner

import ua.place.client.process.ClientProcess
import ua.place.server.process.ServerProcess

class ApplicationRunner {
    def dataManingProcess = new ServerProcess() //server
    def consoleProcess = new ClientProcess()//клиент

    def run() {
        def isCosed = false

        while (!isCosed) {

            def incomeDate = consoleProcess.changeIncomeData() //запросить у клиета данные
            def outcomeData = dataManingProcess.getOutcomeData(incomeDate) //получить данные от сервера

            /** ***************/
            format(incomeDate,outcomeData)
            if(consoleProcess.reply("Get other data?")){
               def incomeDataWithToken=consoleProcess.changeIncomeData(incomeDate,outcomdata)
               def extraOutcomeData = dataManingProcess.getOutcomeData(incomeDataWithToken)
                format(incomeDataWithToken,extraOutcomeData)
            }
            isCosed = !consoleProcess.reply("Change requst?")
        }
    }

    //работа клиента по форматированию полученный данных
    def format(incomData, outcomeData){
        def inData = incomData
        def replayFormat = true
        while (replayFormat) {
            def formatedOutData = consoleProcess.formatData(inData, outcomeData) //отформатировать данные сервера
            print(incomData, formatedOutData)//напечатать входные данные
            replayFormat = consoleProcess.reply("Format replay?")
            if (replayFormat) {
                inData = consoleProcess.changeIncomeData(incomData) //получаем новые данные filterBy и SortedBy
            }
        }
    }

    //печать полученных данных
    def print(incomData, outcomeData) {
        def consoleProcess = new ClientProcess()
        consoleProcess.printIncomeData(incomData)//напечатать входные данные
        consoleProcess.printOutcomeData(outcomeData) //напечатать результат
    }
}