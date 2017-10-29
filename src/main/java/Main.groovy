import ua.place.GooglePlace
import ua.place.NotReceivedException
import ua.place.Place

//def test=[new Place(id:2,name:"one"),new Place(id:4,name:"two"),new Place(id:1,name:"check")]
googlePlace = new GooglePlace(latitude: 48.45925, longitude: 35.04497)
def printResult={result->
    result.each{
        assert it instanceof Place
        println "${it.name}:(${it.vicinity}):${it.types}\n"
    }
}

try {
    def result = googlePlace.
            request().
            request().
            request().
            searchAll().
            sortedByField("distance").
            limit(1).
            loadResult()

    assert result[0] instanceof Place
    googlePlace.requestDetails(result[0] as Place)
    printResult(result)
} catch (NotReceivedException e) {
    println e.message
}




