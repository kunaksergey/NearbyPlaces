package ua.place.entity

import ua.place.config.Config

class InсomeData {
    Location location
    Integer limitPages=Config.DEFAULT_LIMIT_PAGES
    String sortedBy
    List filterBy
}
