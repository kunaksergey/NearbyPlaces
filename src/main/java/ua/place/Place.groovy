package ua.place

class Place {
    def id
    def name
    def vicinity
    def types=[]
    private BigDecimal latitude
    private BigDecimal longitude


    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
