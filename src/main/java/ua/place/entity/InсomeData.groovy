package ua.place.entity

import groovy.transform.Immutable
import ua.place.config.Config


class InсomeData {
    Location location
    Integer limitPages=Config.DEFAULT_LIMIT_PAGES
    String sortedByField
    String filterBy
}
