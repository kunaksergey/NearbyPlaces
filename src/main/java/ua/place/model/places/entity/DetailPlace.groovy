package ua.place.model.places.entity

class DetailPlace {
    def rating
    def icon

    @Override
    String toString() {
        return "DetailPlace{" +
                "rating=" + rating +
                ", icon=" + icon +
                '}'
    }
}
