package ua.place.service

class PrinterData {
    def userData

    def printAll(){
        userData.listPlace.each{
        println it
        }
    }

    def printOne(){
         println userData.listPlace[0]
    }

    def printLog(){
        userData.log.listMessage.each{
            println it
        }
    }
}
