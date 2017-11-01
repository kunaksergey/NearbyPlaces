package ua.place.ui

import ua.place.entity.data.IncomeData
import ua.place.parser.AnswerIncomeDataParser

class ConsoleDialog {
    def answerParser = new AnswerIncomeDataParser()

    def getIncomeData() {
        def answersMap = dialog()
        answerParser.parseAnswers(answersMap)
    }

    def getIncomeData(incomeData) {
        assert incomeData instanceof IncomeData
        def answersMap = [:]
        answersMap['latitude'] = incomeData.location.latitude
        answersMap['longitude'] = incomeData.location.longitude
        answersMap['limit'] =incomeData.limitPages
        answersMap << shotDialog()
        answerParser.parseAnswers(answersMap)
    }

    private def dialog() {
        def answersMap = [:]
        def scanner = new Scanner(System.in)
        print "Latitude (max/min +90 to -90):"
        answersMap['latitude'] = scanner.nextLine()
        print "Longitude (max/min +180 to -180):"
        answersMap['longitude'] = scanner.nextLine()
        print "Limit(1-3): "
        answersMap['limit'] = scanner.nextLine()
        answersMap << shotDialog()
        return answersMap
    }

    private def shotDialog() {
        def answersMap = [:]
        def scanner = new Scanner(System.in)
        print "filters (f1 f2 ...): "
        answersMap['filterBy'] = scanner.nextLine()
        print "sort:"
        answersMap['sortedBy'] = scanner.nextLine()
        return answersMap
    }

    def reply(def message) {
        println "$message (Y/N)"
        def scanner = new Scanner(System.in)
        return (scanner.nextLine().toLowerCase() == 'y')
    }

}
