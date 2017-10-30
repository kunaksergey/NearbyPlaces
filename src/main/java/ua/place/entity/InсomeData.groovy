package ua.place.entity

import groovy.transform.Immutable
import ua.place.config.Config

@Immutable
class InсomeData {
    Location location
    Integer limitPages=Config.DEFAULT_LIMIT_PAGES
    String sortedBy
    String filterBy
}
