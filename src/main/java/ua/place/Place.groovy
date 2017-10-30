package ua.place

class Place {
    def id
    def placeId
    def name
    def vicinity
    def distance
    def types=[]
    BigDecimal latitude
    BigDecimal longitude
    Detail detail=new Detail()

    @Override
     String toString() {
        return  "name=" + name +
                ", vicinity=" + vicinity +
                ", distance=" + distance +
                ", types=" + types +
                ", detail=" + detail
    }


    class Detail {
        def rating
        def icon

        @Override
         String toString() {
            return "Detail{" +
                    "rating=" + rating +
                    ", icon=" + icon +
                    '}'
        }
    }
}
