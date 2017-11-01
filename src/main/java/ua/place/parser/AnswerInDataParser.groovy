package ua.place.parser

import ua.place.config.Config
import ua.place.entity.data.IncomeData
import ua.place.entity.place.Location

/**
 * Created by sa on 31.10.17.
 */
class AnswerInDataParser {

    def parseAnswers(answers) throws NumberFormatException {
        assert answers instanceof Map
        def latitude = Config.DEFAULT_LATITUDE
        def longitude = Config.DEFAULT_LONGITUDE
        def limit = Config.DEFAULT_PAGES
        def filterBy=[]+Config.DEFAULT_FILTER_BY
        def sortedBy = Config.DEFAULT_SORT_BY
        try {
            def lat = answers['latitude'] as BigDecimal
            def lon = answers['longitude'] as BigDecimal

            latitude = (lat < 90 && lat > -90) ? lat : 0
            longitude = (lon < 180 && lon > -180) ? lon : 0
            if (answers['limit'] != "") {
                def lim = answers['limit'] as Integer
                if (lim > Config.MAX_PAGES) limit = Config.MAX_PAGES
                if (lim < 1) limit = 1
            }
        } catch (NumberFormatException ex) {
            //do nothing
        }
        if (answers['filterBy'] != "") {
            def filters = (answers['filterBy'] as String).split(' ')
            if (filters.size() > 0) {
                filterBy = filters
            }
        }

        if (answers['sortedBy'] != "") {
            sortedBy = answers['sortedBy']
        }
        return new IncomeData(location: new Location(latitude: latitude, longitude: longitude),
                limitPages: limit, filterBy: filterBy, sortedBy: sortedBy)
    }

}
