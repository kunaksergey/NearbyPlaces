package ua.place.client.factory

import ua.place.entity.place.Location
import ua.place.entity.quary.Request
import ua.place.entity.quary.Response

/**
 * Created by sa on 31.10.17.
 */
class QuaryCreator {

    def createQuary(answerMap) {
        assert answerMap instanceof Map
        try {
            def latitude = answerMap['latitude'] as BigDecimal
            def longitude = answerMap['longitude'] as BigDecimal
            def radius = answerMap['radius'] as Integer
            return new Request(location: new Location(latitude: latitude, longitude: longitude), radius: radius)
        } catch (NumberFormatException ex) {
            ex.printStackTrace()
        }
    }

    def createQuary(request, response) {
        assert request instanceof Request
        assert response instanceof Response
        def requestClone = request.clone() as Request
        if (request.next_page_token != null) {
            requestClone.next_page_token = response.next_page_token
        }
        return requestClone
    }
}
