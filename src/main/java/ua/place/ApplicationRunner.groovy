package ua.place

import ua.place.client.process.ClientProcess

class ApplicationRunner {
    def clientProcess = new ClientProcess()//клиент

    def run() {
        def answerMap=clientProcess.dialog() //запросить у клиета данные
        def isClosed = !clientProcess.validate(answerMap) //проверяем данные от клиента
        while (!isClosed) {

            def firstRequest = clientProcess.createRequest(answerMap) //получить запрос
            //потом удалить
            // def firstRequest = new Request(radius: 1000,location: new Location(latitude:-33.70522,longitude:151.1957362))

            //получить данные от сервера
            def firstResponse= clientProcess.getResponse(firstRequest)

            //Если есть данные в ответе
            if (firstResponse.places.size() != 0) {
                    //отпраляем на внутреннюю обработку
                clientProcess.handleResponse(firstResponse)

                if (clientProcess.hasNextData(firstResponse)&&
                                    clientProcess.yesOrNot("Get extended quary?")) {
                    def nextRequest = clientProcess.createRequest(firstRequest,firstResponse) //формируем новый запрос
                    //получить роcширенные данные от сервера
                    def nextResponse = clientProcess.getResponse(nextRequest)
                    //отпраляем на внутреннюю обработку
                    clientProcess.handleResponse(nextResponse)

                }
            }
            isClosed = !clientProcess.yesOrNot("Improove requst?")
            if(!isClosed){
               answerMap<<clientProcess.shortDialog()
            }
        }
    }
}