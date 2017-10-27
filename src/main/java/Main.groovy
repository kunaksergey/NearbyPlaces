import ua.place.GooglePlace
import ua.place.Place
//def test=[new Place(id:2,name:"one"),new Place(id:4,name:"two"),new Place(id:1,name:"check")]
googlePlace=new GooglePlace(latitude:48.45925,longitude:35.04497,radius:500,limit:21)

printPlace(googlePlace.findPlaces())
println "------------------"

printPlace(googlePlace.findPlaceSortedByField("name"))
println "------------------"

printPlace(googlePlace.findPlaceSortedByField("vicinity"))
println "------------------"

void printPlace(def list){
    list.each{
        println "${it.name}-${it.vicinity}-${it.types}"
    }
}

