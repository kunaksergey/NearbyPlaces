import ua.place.GooglePlace
import ua.place.NotReceivedException
import ua.place.Place

//def test=[new Place(id:2,name:"one"),new Place(id:4,name:"two"),new Place(id:1,name:"check")]
googlePlace = new GooglePlace(latitude: 48.45925, longitude: 35.04497)
//def r=googlePlace.findPlaces()
//printPlace(googlePlace.findPlaceSortedByField("distance"))
try {
    def result = googlePlace.next().
            next().searchOne().
            sortedByField("distance").
            limit(1).loadResult()
    assert result[0] instanceof Place
    GooglePlace.loadDetails(result[0] as Place)
    println result[0]
} catch (NotReceivedException e) {
    println e.message
}




