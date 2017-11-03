package ua.place.client.tools

import ua.place.entity.transport.Response

class PrintData {

    def printResponse(response) {
        assert response in Response
        if (response.places.size() == 0) {
            println "[]"
        }
        println response.status
        printPlaces(response.places)
    }

    def printPlaces(places) {
        if (places.size() == 0) {
            println "[]"
        }
        assert places instanceof List
        places.each {
            println it
        }
    }
}

