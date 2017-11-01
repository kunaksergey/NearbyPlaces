package ua.place

import ua.place.entity.data.IncomeData
import ua.place.entity.data.OutcomeData
import ua.place.exception.GoogleException
import ua.place.http.GooglePagesClient
import ua.place.format.FilterAndSortPlaces
import ua.place.parser.JsonPlaceParser

class HandlerData {
    def googleClient = new GooglePagesClient()
    def jsonParser = new JsonPlaceParser()
    def pages

    def handle(incomeData) throws GoogleException {
        assert incomeData instanceof IncomeData
        if (pages == null) {
            pages = googleClient.requestPages(incomeData)
        }
        def formatedPlaces = []
        def parsedPlaces = jsonParser.parsePages(incomeData.location, pages)
        if (parsedPlaces != null) {
            formatedPlaces += new FilterAndSortPlaces(places: parsedPlaces).
                    filter(incomeData.filterBy).
                    sort(incomeData.sortedBy).places
        }
        return new OutcomeData(places: formatedPlaces)
    }
}
