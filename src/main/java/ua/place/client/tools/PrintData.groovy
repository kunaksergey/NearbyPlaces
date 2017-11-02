package ua.place.client.tools

import ua.place.entity.quary.Response

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
        assert places instanceof List
        places.each {
            println it
        }
    }
}

