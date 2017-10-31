import ua.place.entity.InсomeData
import ua.place.entity.Location
import ua.place.exception.NotFieldException
import ua.place.exception.NotTypeException
import ua.place.service.GooglePageRecipient
import ua.place.service.HandlerRecipient
import ua.place.service.Parser
import ua.place.service.PrinterData

def latitude = 48.5123967
def longitude = 35.0844862
def limitPages = 1
def filterBy = "store"
def sortedBy = "distance"

def incomeData = new InсomeData(
        location: new Location(latitude: latitude, longitude: longitude),
        limitPages: limitPages,
        filterBy: filterBy,
        sortedBy: sortedBy)
log = []
def pageRecipient = new GooglePageRecipient()
def googlePages = pageRecipient.requestPages(incomeData)
def listPlace = new Parser(incomeData: incomeData).parsePages(googlePages)

def handlerRecipient = new HandlerRecipient()
//def listFilteredPlace = handlerRecipient.filterByType(incomeData.filterBy, listPlace)
def listFilteredPlace
try {
    listFilteredPlace = handlerRecipient.filterByType(incomeData.filterBy, listPlace)
    log << "filtered:OK"
} catch (NotTypeException e) {
    listFilteredPlace = listPlace.collect()
    log << e.message
}
def listSortedPlace
try {
    listSortedPlace = handlerRecipient.sortedByField(incomeData.sortedBy, listFilteredPlace)
    log << "sorted:OK"
} catch (NotFieldException e) {
    listSortedPlace = listPlace.collect()
    log << e.message
}

def printData = new PrinterData()
printData.printOne(listSortedPlace[0])
println "*******************************"
printData.printAll(listSortedPlace)

