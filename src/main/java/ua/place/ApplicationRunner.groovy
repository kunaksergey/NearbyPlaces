package ua.place

import ua.place.client.process.ClientProcess
import ua.place.entity.quary.Request
import ua.place.server.process.ServerProcess

class ApplicationRunner {
    def serverProcess = new ServerProcess() //server
    def clientProcess = new ClientProcess()//клиент

    def run() {
        def isCosed = false

        while (!isCosed) {

            def firstRequest = clientProcess.createQuary() //запросить у клиета данные
            assert firstRequest instanceof Request//валидный ли запрос
            def firstResponse = serverProcess.getResponse(firstRequest) //получить данные от сервера

            //Если есть данные в ответе
            if (firstResponse.places.size() != 0) {

                clientProcess.handleResponse(firstResponse)

                if (clientProcess.yesOrNot("Get extended quary?")) {
                    def secondRequest = clientProcess.createQuary(firstRequest,firstResponse) //формируем новый запрос
                    assert firstRequest instanceof Request//валидный ли запрос
                    def secondResponse = serverProcess.getResponse(secondRequest)
                    //получить розширенные данные от сервера
                    clientProcess.handleResponse(secondResponse)

                }
            }
            isCosed = !clientProcess.yesOrNot("Change requst?")
        }
    }
}