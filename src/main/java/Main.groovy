import ua.place.GooglePlace
import ua.place.exception.NotReceivedException
import ua.place.entity.Place

try {
    def googlePlace = new GooglePlace(latitude: 48.5123967, longitude: 35.0844862)
    def places = googlePlace.
            request().
            //sortedByField("distance").
            limit(10).
           // filterByType("store").
            loadResult()
     assert places[0] instanceof Place
    googlePlace.requestDetails(places[0] as Place)
    println places[0]
    println '******************************'
    printResult(places)
} catch (NotReceivedException e) {
    println e.message
}

/*********************/
def printResult(result){
    result.each{
        assert it instanceof Place
        println "${it.name}:(${it.vicinity}):${it.types}:${it.distance}"
    }
}






