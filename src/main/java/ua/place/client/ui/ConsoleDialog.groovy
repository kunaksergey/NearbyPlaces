package ua.place.client.ui

class ConsoleDialog {

     def dialog() {
        def answersMap = [:]
        def scanner = new Scanner(System.in)
        print "Latitude (max/min +90 to -90):"
        answersMap['latitude'] = scanner.nextLine()
        print "Longitude (max/min +180 to -180):"
        answersMap['longitude'] = scanner.nextLine()
        print "Radius: "
        answersMap['radius'] = scanner.nextLine()
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

    def reply(def message) {
        println "$message (Y/N)"
        def scanner = new Scanner(System.in)
        return (scanner.nextLine().toLowerCase() == 'y')
    }
}
