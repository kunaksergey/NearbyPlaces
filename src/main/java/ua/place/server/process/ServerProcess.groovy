package ua.place.server.process

import ua.place.entity.data.IncomeData
import ua.place.entity.data.OutcomeData
import ua.place.server.enumer.StatusCodeEnum
import ua.place.server.exception.GoogleException
import ua.place.server.parser.JsonPlaceParser
import ua.place.server.http.GooglePagesClient

//SERVER
class ServerProcess {
    def googleClient = new GooglePagesClient()
    def jsonParser = new JsonPlaceParser()


    def getOutcomeData(incomeData) {


        assert incomeData instanceof IncomeData
        try {
            if (incomeData.next_page_token == null) {
                def pages = googleClient.requestOnePage(incomeData)
                def parsedPlaces = jsonParser.parsePages(incomeData.location, page)
                return new OutcomeData(places: parsedPlaces,
                        status: pages[0].status,
                        token: pages[0].next_page_token)
            } else {
                def pages = googleClient.requestAllPages(incomeData)
                def parsedPlaces = jsonParser.parsePages(incomeData.location, pages)
                return new OutcomeData(places: parsedPlaces,
                        status: pages[pages.size() - 1].status)
            }
        } catch (GoogleException ex) {
            return new OutcomeData(places: [], status: StatusCodeEnum.GOOGLE_ERROR)
        }
    }
}
