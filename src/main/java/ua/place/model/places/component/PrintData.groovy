package ua.place.model.places.component

/**
 * Created by sa on 04.11.17.
 */
class PrintData {
    def printPlaces(places, status,token) {
        assert places instanceof List
        println "status: "+status
        print "token: "+token
        if (places.size() == 0) {
            println "[]"
        }

        places.each {
            println it
        }
    }
}