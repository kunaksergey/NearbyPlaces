package ua.place.ui

import ua.place.parser.AnswerIncomeDataParser

class ConsoleDialog {
    def answerParser = new AnswerIncomeDataParser()

    def getIncomeData(){
        def answersMap = dialog()
        answerParser.parseAnswers(answersMap)
    }
    def dialog() {
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

    def shotDialog() {
        def answersMap = [:]
        def scanner = new Scanner(System.in)
        print "filters (f1 f2 ...): "
        answersMap['filterBy'] = scanner.nextLine()
        print "sort:"
        answersMap['sortedBy'] = scanner.nextLine()
        return answersMap
    }

    def reply() {
        println 'Continue?(Y/N)'
        def scanner = new Scanner(System.in)
        return (scanner.nextLine().toLowerCase() == 'y')
    }
    def replyRequest() {
        println 'Replay quary to Google?(Y/N)'
        def scanner = new Scanner(System.in)
        return (scanner.nextLine().toLowerCase() == 'y')
    }

}
