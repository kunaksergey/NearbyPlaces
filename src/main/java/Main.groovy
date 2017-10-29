import ua.place.GooglePlace
import ua.place.NotReceivedException
import ua.place.Place

try {
    def googlePlace = new GooglePlace(latitude: 48.45925, longitude: 35.04497)
    def place = googlePlace.
            request().
            request().
            request().
            sortedByField("distance").
          //  filterByType("gym").
            limit(1).
            loadResult()

    assert place[0] instanceof Place
    googlePlace.requestDetails(place[0] as Place)
    printResult(place)
} catch (NotReceivedException e) {
    println e.message
}

/*********************/
def printResult(result){
    result.each{
        assert it instanceof Place
        println "${it.name}:(${it.vicinity}):${it.types}\n"
    }
}




