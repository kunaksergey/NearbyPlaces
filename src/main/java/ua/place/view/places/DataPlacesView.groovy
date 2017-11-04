package ua.place.view.places

import ua.place.model.places.component.PrintData

class DataPlacesView {
    def printData = new PrintData()
    def response

    def print() {
        printData.printPlaces(response.places, response.status,response.next_page_token)
    }

    def next_page_token(){
        return response.next_page_token
    }

}

