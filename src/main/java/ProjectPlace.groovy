import ua.place.entity.InсomeData
import ua.place.entity.Location
import ua.place.entity.UserData
import ua.place.service.GooglePlaceRecipient
import ua.place.service.HandlerRecipient
import ua.place.service.Parser
import ua.place.service.PrinterData

def limitPages = 3
def filterBy = null
def sortedBy = null

incomeData = new InсomeData(location: new Location(latitude: 48.5123967, longitude: 35.0844862),
        limitPages: limitPages,
        filterBy: filterBy,
        sortedBy: sortedBy)

googlePlaceRecipient=new GooglePlaceRecipient()
parser=new Parser()
handlerRecipient=new HandlerRecipient()
printData = new PrinterData()
listJsonPages = googlePlaceRecipient.requestPages(incomeData)
userData = new UserData(listJsonPages)
listPlaceParsered=parser.parsePages(userData.listJsonPages)
listPlaceFiltered=handlerRecipient.filterByType(incomeData.filterBy,listPlaceParsered)
listPlaceSorted=handlerRecipient.sortedByField(incomeData.sortedBy,listPlaceFiltered)
printData.printOne(listPlaceSorted[0])
printData.printAll(listPlaceSorted)

