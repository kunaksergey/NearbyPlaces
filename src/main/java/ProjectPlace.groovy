import ua.place.Runner
import ua.place.entity.InсomeData
import ua.place.entity.Location
import ua.place.entity.UserAnswer

//def latitude = 48.5123967
//def longitude = 35.0844862
//def limitPages =1
//def filterBy = null
//def sortedBy = null

static main(args) {
def latitude = 48.5123967
def longitude = 35.0844862
def limitPages =1
def filterBy = ['store','ss']
def sortedBy = null
    def incomeData = new InсomeData(
            location: new Location(latitude: latitude, longitude: longitude),
            limitPages: limitPages, filterBy: filterBy,sortedBy: sortedBy)

    def userAnswer=new Runner().run(incomeData)

}

//
//
//def log=[]
//def result=[]
//

//try {
//    DumpData dumpData = pageRecipient.requestPages(incomeData)
//    log<<"data:"+StatusCodeEnum.OK
//    def listPlace = parser.parsePages(incomeData.location,dumpData.getDumpResult())
//
//    def listFS=listPlace
//
//    try {
//        listFS = handlerRecipient.filterByType(incomeData.filterBy, listFS)
//        log << "filtered:"+StatusCodeEnum.OK
//    } catch (NotTypeException e) {
//        log <<"filtered:"+e.message
//    }
//
//    try {
//        listFS = handlerRecipient.sortedByField(incomeData.sortedBy, listFS,Place.class)
//        log << "sorted:"+StatusCodeEnum.OK
//    } catch (NotFieldException e) {
//        log << "sorted:"+e.message
//    }
//
//
//    result.addAll(listFS)
//} catch (GoogleException ex) {
//    log << "data:"+ex.message
//}
//if(result[0]!=null){
//    assert result[0] instanceof Place
//    result[0].detail=pageRecipient.requestDetailsPage(result[0].placeId)
//}
//def userAnswer=new UserAnswer(result:result,log:log)
//def printData = new PrinterData()
//printData.printOne(userAnswer.result[0])
//println "******************************"
//printData.printAll(userAnswer.result)
//printData.printAll(userAnswer.log)
