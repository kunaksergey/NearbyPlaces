package ua.place

class Place {
    def id
    def placeId
    def name
    def vicinity
    def distance
    def types=[]
    private BigDecimal latitude
    private BigDecimal longitude
    private Detail detail=new Detail()

    @Override
    String toString() {
        return "Place{" +
                "name=" + name +
                ", distance=" + distance +
                '}';
    }


    class Detail {
        def rating
        def icon
    }
}
