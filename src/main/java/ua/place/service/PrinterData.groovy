package ua.place.service

class PrinterData {

    def printAll(list){
        if(list.size()==0 ){
            println "[]"
        }
        list.each{
        println it
        }
    }

    def printOne(place){
        if(place==null){
             return
        }
         println place
    }
}
