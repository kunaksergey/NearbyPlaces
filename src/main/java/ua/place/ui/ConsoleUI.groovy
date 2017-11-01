package ua.place.ui

class ConsoleUI {


    def dialog() {
        def answersMap = [:]
        System.in.withReader {
            print "Latitude (max/min +90 to -90):"
            answersMap['latitude'] = it.readLine()
            print "Longitude (max/min +180 to -180):"
            answersMap['longitude'] = it.readLine()
            print "Limit(1-3): "
            answersMap['limit'] = it.readLine()
            print "filters (f1 f2 ...): "
            answersMap['filterBy'] = it.readLine()
            print "sort:"
            answersMap['sortedBy'] = it.readLine()
        }
        return answersMap
    }

    def shotDialog() {
        def answersMap = [:]
        System.in.withReader {
            print "filters (f1 f2 ...): "
            answersMap['filterBy'] << it.readLine()
            print "sort:"
            answersMap['sortedBy'] << it.readLine()
        }
        return answersMap
    }

    def reply() {
        def answer
        System.in.withReader {
            print 'Continue?yes-(Y,y),not-(Enter)'
            answer it.readLine()
        }
        return (answer.toLowerCase() == 'y') ? true : false
    }
}
