import org.codehaus.groovy.runtime.InvokerHelper
import ua.place.parser.IncomeDataParser
import ua.place.runner.CommandLineRunner
import ua.place.service.PrinterData

answers=dialog()

    try{
    def incomeData=new IncomeDataParser().parseArgs(answers)
    def userAnswer=new CommandLineRunner().run(incomeData)
    def printData = new PrinterData()
    printData.printOne(userAnswer.places[0])
    println "******************************"
    printData.printAll(userAnswer.places)
    }catch (NumberFormatException ex){
        //как-то обработать
    }

def dialog(){
    System.in.withReader {
        def answers=[]
        print "Latitude : max/min +90 to -90:"
        answers<< it.readLine()
        print "Longitude : max/min +180 to -180:"
        answers<< it.readLine()
        print "pages limit(1-3):"
        answers<< it.readLine()
        print "filters (f1 f2 ...): "
        answers<< it.readLine()
        print "sort:"
        answers<< it.readLine()
    }

}

def shotDialog(){
    def answers=[]
    System.in.withReader {
        print "filters (f1 f2 ...): "
        answers << it.readLine()
        print "sort:"
        answers << it.readLine()
    }
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
//def userAnswer=new OutcomeData(result:result,log:log)
//def printData = new PrinterData()
//printData.printOne(userAnswer.result[0])
//println "******************************"
//printData.printAll(userAnswer.result)
//printData.printAll(userAnswer.log)
