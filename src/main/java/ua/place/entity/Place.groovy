package ua.place.entity

class Place {
    def id
    def placeId
    def name
    def vicinity
    BigDecimal distance
    def types=[]
    def location=new Location()
    def detail=new Detail()

    @Override
     String toString() {
        return  "name=" + name +
                ", vicinity=" + vicinity +
                ", distance=" + distance +
                ", types=" + types +
                ", detail=" + detail

    }

}
