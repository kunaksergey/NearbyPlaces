package ua.place.client.tools

import ua.place.entity.data.IncomeData
import ua.place.entity.data.OutcomeData

class PrintData {

    private printAll(outcomeData){
        assert outcomeData in OutcomeData
        if(outcomeData.places.size()==0 ){
            println "[]"
        }
        outcomeData.places.each{
        println it
        }
    }

    private def printOne(outcomeData){
        assert outcomeData in OutcomeData
        if(outcomeData.places[0]==null){
             return
        }
         println outcomeData.places[0]
    }
    def printOutcomeData(outcomeData){
        assert outcomeData in OutcomeData
        println()
        printOne(outcomeData)
        println "************************"
        printAll(outcomeData)
    }
    def printIncomeData(incomeData){
        assert incomeData in IncomeData
        println()
        print("latitude: $incomeData.location.latitude, longitude:$incomeData.location.longitude," +
                " limit: $incomeData.limitPages, filtered by: $incomeData.filterBy, sorted by: $incomeData.sortedBy")
        println()
    }
}

