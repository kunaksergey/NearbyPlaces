package ua.place.server.process

import ua.place.entity.quary.Request
import ua.place.entity.quary.Response
import ua.place.server.enumer.StatusCodeEnum
import ua.place.server.exception.GoogleException
import ua.place.server.parser.JsonPlaceParser
import ua.place.server.http.GooglePagesClient

//SERVER
class ServerProcess {
    def googleClient = new GooglePagesClient()
    def jsonParser = new JsonPlaceParser()


    def getResponse(request) {

        assert request instanceof Request
        try {
            if (request.next_page_token == null) {
                def pages = googleClient.requestOnePage(request)
                def parsedPlaces = jsonParser.parsePages(request.location, pages)
                return new Response(places: parsedPlaces,
                        status: pages[0].status,
                        next_page_token: pages[0].next_page_token)
            } else {
                def pages = googleClient.requestAllPages(request)
                def parsedPlaces = jsonParser.parsePages(request.location, pages)
                return new Response(places: parsedPlaces,
                        status: pages[pages.size() - 1].status)
            }
        } catch (GoogleException ex) {
            return new Response(places: [], status: StatusCodeEnum.GOOGLE_ERROR)
        }
    }
}
