package ua.place.parser

import ua.place.entity.IncomeData
import ua.place.entity.Location

import java.util.zip.DataFormatException

/**
 * Created by sa on 31.10.17.
 */
class IncomeDataParser {

    public def parseArgs(args) throws NumberFormatException {
        def limitPages = 1
        def filterBy = []
        def sortedBy = null
        if (args.size() < 2) throw new NumberFormatException("at least latitude and longitude")
        def latitude = args[0] as BigDecimal
        def longitude = args[1] as BigDecimal

        if (args[3] != null) {
            limitPages = args.getAt(2) as Integer
        }
        if (args.size() >= 4) {
            filterBy += args[3]
        }
        if (args.size() >= 5) {
            sortedBy = args[4] as String
        }
        return new IncomeData(location:new Location(latitude: latitude, longitude: longitude),
                limitPages: limitPages, filterBy: filterBy, sortedBy: sortedBy)
    }
}
