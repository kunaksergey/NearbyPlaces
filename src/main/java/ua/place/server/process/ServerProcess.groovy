package ua.place.server.process

import ua.place.entity.quary.Request
import ua.place.entity.quary.Response
import ua.place.server.enumer.StatusCodeEnum
import ua.place.server.exception.GoogleException
import ua.place.server.http.GooglePagesClient
import ua.place.server.parser.JsonPlaceParser
import ua.place.server.validator.RequestValidator

//SERVER
class ServerProcess {
    def googleClient = new GooglePagesClient()
    def jsonParser = new JsonPlaceParser()
    def validator = new RequestValidator()


    def getResponse(request) {

        assert request instanceof Request
        try {
            //если не прошли валидацию входных данных
            if (!validator.validate(request)) {
                return new Response(places: [], status: StatusCodeEnum.INVALID_REQUEST)
            }
            if (request.next_page_token == null) {

                def pages = googleClient.requestOnePage(request)
                def parsedPlaces = jsonParser.parsePages(request.location, pages.results)
                return new Response(places: parsedPlaces,
                        status: pages.status,
                        next_page_token: pages.next_page_token)
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
