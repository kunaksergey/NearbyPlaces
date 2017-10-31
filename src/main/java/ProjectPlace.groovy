import ua.place.entity.InсomeData
import ua.place.entity.Location
import ua.place.entity.Place
import ua.place.entity.UserAnswer
import ua.place.enumer.StatusCodeEnum
import ua.place.exception.GoogleException
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException
import ua.place.service.GooglePageRecipient
import ua.place.service.HandlerRecipient
import ua.place.service.Parser
import ua.place.service.PrinterData

def latitude = 48.5123967
def longitude = 35.0844862
def limitPages =3
def filterBy = null
def sortedBy = "distances"

def incomeData = new InсomeData(
        location: new Location(latitude: latitude, longitude: longitude),
        limitPages: limitPages,
        filterBy: filterBy,
        sortedBy: sortedBy)
def log=[]
def result=[]
def pageRecipient = new GooglePageRecipient()
def handlerRecipient = new HandlerRecipient()
def parser=new Parser()
try {
    def googlePages = pageRecipient.requestPages(incomeData)
    log<<"data:"+StatusCodeEnum.OK
//    def listPlace = parser.parsePages(incomeData.location,googlePages)
      def listPlace=googlePages.results[0]

    def listFilteredPlace

    try {
        listFilteredPlace = handlerRecipient.filterByType(incomeData.filterBy, listPlace)
        log << "filtered:"+StatusCodeEnum.OK
    } catch (NotTypeException e) {
        listFilteredPlace = listPlace.collect()
        log <<"filtered:"+e.message
    }

    def listSortedPlace
    try {
        listSortedPlace = handlerRecipient.sortedByField(incomeData.sortedBy, listFilteredPlace,Place.class)
        log << "sorted:"+StatusCodeEnum.OK
    } catch (NotFieldException e) {
        listSortedPlace = listFilteredPlace.collect()
        log << "sorted:"+e.message
    }

    result.addAll(listSortedPlace)
} catch (GoogleException ex) {
    log << "data:"+ex.message
}

def userAnswer=new UserAnswer(result:result,log:log)
def printData = new PrinterData()
printData.printOne(userAnswer.result[0])
println "******************************"
printData.printAll(userAnswer.result)
printData.printAll(userAnswer.log)
