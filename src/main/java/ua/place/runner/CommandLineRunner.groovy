package ua.place.runner

import ua.place.entity.IncomeData
import ua.place.entity.UserAnswer
import ua.place.http.GooglePagesClient
import ua.place.manipulation.FormatPlaceChain
import ua.place.parser.JsonPlaceParser

class CommandLineRunner {

    def run(incomeDate) {
        assert incomeDate instanceof IncomeData
        def googlePages = new GooglePagesClient().requestPages(incomeDate)
        def places = new JsonPlaceParser().parsePages(incomeDate.location, googlePages)
        def placeChain = new FormatPlaceChain(places: places).
                filter(incomeDate.filterBy).
                sort(incomeDate.sortedBy)
        new UserAnswer(places: placeChain.places)
    }
}
