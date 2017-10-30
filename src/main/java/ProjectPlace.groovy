import ua.place.entity.InсomeData
import ua.place.entity.Location
import ua.place.entity.UserData
import ua.place.service.GooglePlaceRecipient
import ua.place.service.HandlerRecipient
import ua.place.service.Parser
import ua.place.service.PrinterData

def limitPages = 1
def filterBy = null
def sortedBy = null

def incomeData = new InсomeData(location: new Location(latitude: 48.5123967, longitude: 35.0844862),
        limitPages: limitPages,
        filterBy: filterBy,
        sortedBy: sortedBy)

def googlePlaceRecipient=new GooglePlaceRecipient()
def parser=new Parser(incomeData: incomeData)
def handlerRecipient=new HandlerRecipient()
def printData = new PrinterData()

def listJsonPages = googlePlaceRecipient.requestPages(incomeData)
def userData = new UserData(listJsonPages)

def parsedList=parser.parsePages(userData.listJsonPages)
def filteredList=handlerRecipient.filterByType(incomeData.filterBy,parsedList)
sortedList=handlerRecipient.sortedByField(incomeData.sortedBy,filteredList)

printData.printOne(sortedList[0])
println "*******************************"
printData.printAll(sortedList)

