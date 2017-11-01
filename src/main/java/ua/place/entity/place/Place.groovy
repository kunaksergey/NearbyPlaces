package ua.place.entity.place

class Place {
    def id
    def placeId
    def name
    def vicinity
    BigDecimal distance
    def types=[]
    def location
    def detail

    @Override
     String toString() {
        return  "name=" + name +
                ", vicinity=" + vicinity +
                ", distance=" + distance +
                ", types=" + types +
                ", detail=" + detail

    }
}
