package ua.place.entity

class DumpData {
    private def pages

    def getDumpResult(){
        List list = []
        pages.results.each {
            list.addAll(it)
        }
        return list
    }
}
