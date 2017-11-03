package ua.place.client.handler

/**
 * Created by sa on 03.11.17.
 */
class HandlerData {
    def getPlaces(response){
        return response.places
    }

    def getStatus(response){
        return response.status
    }
    def getToken(response){
        return response.next_page_token
    }
}
