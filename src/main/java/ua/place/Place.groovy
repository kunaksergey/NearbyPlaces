package ua.place

class Place {
    def id
    def placeId
    def name
    def vicinity
    def distance
    def types=[]
    def BigDecimal latitude
    def BigDecimal longitude
    def Detail detail=new Detail()

    @Override
    public String toString() {
        return "Place{" +
                "name=" + name +
                ", vicinity=" + vicinity +
                ", distance=" + distance +
                ", types=" + types +
                ", detail=" + detail +
                '}';
    }


    class Detail {
        def rating
        def icon

        @Override
        public String toString() {
            return "Detail{" +
                    "rating=" + rating +
                    ", icon=" + icon +
                    '}';
        }
    }
}
