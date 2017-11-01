package ua.place.factory

import ua.place.config.Config
import ua.place.entity.data.IncomeData
import ua.place.entity.place.Location

/**
 * Created by sa on 31.10.17.
 */
class IncomeDataCreator {

    def createIncomeData(answerMap){
        assert answerMap instanceof Map
        def latitude = Config.DEFAULT_LATITUDE
        def longitude = Config.DEFAULT_LONGITUDE
        def limit = Config.DEFAULT_PAGES
        def filterBy=null
        def sortedBy = null
        try {
            def lat = answerMap['latitude'] as BigDecimal
            def lon = answerMap['longitude'] as BigDecimal

            latitude = (lat < 90 && lat > -90) ? lat : 0
            longitude = (lon < 180 && lon > -180) ? lon : 0
            if (answerMap['limit'] != "") {
                def lim = answerMap['limit'] as Integer
                if (lim > Config.MAX_PAGES) limit = Config.MAX_PAGES
                if (lim < 1) limit = 1
            }
        } catch (NumberFormatException ex) {
            //do nothing
        }
        if (answerMap['filterBy'] != "") {
            def filters = (answerMap['filterBy'] as String).split(' ')
            if (filters.size() > 0) {
                filterBy = filters
            }
        }

        if (answerMap['sortedBy'] != "") {
            sortedBy = answerMap['sortedBy']
        }
        return new IncomeData(location: new Location(latitude: latitude, longitude: longitude),
                limitPages: limit, filterBy: filterBy, sortedBy: sortedBy)
    }

}
