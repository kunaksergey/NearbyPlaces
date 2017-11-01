package ua.place.process

import ua.place.entity.data.IncomeData
import ua.place.factory.IncomeDataCreator
import ua.place.ui.ConsoleDialog

class ConsoleProcess {
    def incomeDataCreator = new IncomeDataCreator()
    def consoleDialog = new ConsoleDialog()
    //def answerMap

    def getIncomeData() {
//       if (answerMap != null) {
//            def answerMapClone = this.answerMap.clone()
//            answerMapClone << consoleDialog.shotDialog()
//            return incomeDataCreator.createIncomeData(answerMapClone)
//          } else {
//            answerMap = consoleDialog.dialog()
//            return incomeDataCreator.createIncomeData(answerMap)
//        }
        def answerMap = consoleDialog.dialog()
        return incomeDataCreator.createIncomeData(answerMap)
    }

    def getIncomeData(incomeData) {
        assert incomeData instanceof IncomeData
        def answersMap = [:]
        answersMap['latitude'] = incomeData.location.latitude
        answersMap['longitude'] = incomeData.location.longitude
        answersMap['limit'] = incomeData.limitPages
        answersMap << consoleDialog.shotDialog()
        incomeDataCreator.createIncomeData(answersMap)
    }

    def reply(def message) {
        consoleDialog.reply(message)
    }

}
