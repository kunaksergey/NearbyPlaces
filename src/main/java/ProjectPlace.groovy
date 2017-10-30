import ua.place.entity.InсomeData
import ua.place.entity.Location
import ua.place.entity.UserData
import ua.place.service.GooglePlaceRecipient
import ua.place.service.HandlerRecipient
import ua.place.service.Parser
import ua.place.service.PrinterData
def limitPages=1
def filterBy=null
def sortedByField=null

incomeData=new InсomeData(location:  new Location(latitude:48.5123967,longitude:35.0844862),
                          limitPages:limitPages,
                          filterBy:filterBy,
                          sortedByField:sortedByField)

userData=new UserData(incomeData:incomeData)

googlePlaceRecipient=new GooglePlaceRecipient(userData:userData)

parser=new Parser(userData:userData)

handlerRecipient=new HandlerRecipient(userData:userData)

printData=new PrinterData(userData:userData)

run_()

def run_(){
    googlePlaceRecipient.request()
    parser.parse()
    handlerRecipient.filterByType()
    handlerRecipient.sortedByField()
    printData.printOne()
    println "*****************"
    printData.printAll()
    println "*****************"
    printData.printLog()
}


