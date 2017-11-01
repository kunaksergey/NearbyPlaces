package ua.place.process

import ua.place.entity.data.IncomeData
import ua.place.entity.data.OutcomeData
import ua.place.format.FilterAndSortPlaceChain
import ua.place.http.GooglePagesClient
import ua.place.parser.JsonPlaceParser

class DataManingProcess {
    def googleClient = new GooglePagesClient()
    def jsonParser = new JsonPlaceParser()
    def pages

    def getOutcomeData(incomeData) {
        assert incomeData instanceof IncomeData
        if (pages == null) {
            pages = googleClient.requestPages(incomeData)
        }
        def formatedPlaces = []
        def parsedPlaces = jsonParser.parsePages(incomeData.location, pages)
        if (parsedPlaces != null) {
            formatedPlaces += new FilterAndSortPlaceChain(places: parsedPlaces).
                    filter(incomeData.filterBy).
                    sort(incomeData.sortedBy).places
        }
        return new OutcomeData(places: formatedPlaces)
    }
}
