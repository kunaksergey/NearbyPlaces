import ua.place.GooglePlace
import ua.place.Place
//def test=[new Place(id:2,name:"one"),new Place(id:4,name:"two"),new Place(id:1,name:"check")]
googlePlace=new GooglePlace(latitude:48.45925,longitude:35.04497,radius:500,limit:21)
//def r=googlePlace.findPlaces()
//printPlace(googlePlace.findPlaceSortedByField("distance"))
def result=googlePlace.next().searchCurrentPage().loadResult()
println "xxx"
void printPlace(def list){
    list.each{
        println "${it.name}-${it.distance}"
    }
}

