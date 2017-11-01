package ua.place.runner
import ua.place.ui.ConsoleUI
import ua.place.HandlerData
import ua.place.parser.AnswerInDataParser
import ua.place.tools.PrintData

class ConsoleRunner {
    def run() {
        def consoleUI = new ConsoleUI()
        def handler = new HandlerData()
        def printerData = new PrintData()
        def answerParser = new AnswerInDataParser()
        def replay = true
        def answers = consoleUI.dialog()
        while (replay) {
            def incomeDate = answerParser.parseAnswers(answers)
            def outcomeData = handler.handle(incomeDate)
            printerData.printIncomeData(incomeDate)
            printerData.printOutcomeData(outcomeData)
            if(consoleUI.reply()){

            }

            replay = consoleUI.reply()
        }
    }
}