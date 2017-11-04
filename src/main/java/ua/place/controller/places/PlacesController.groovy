package ua.place.controller.places

import ua.place.model.places.component.FormatAndSort
import ua.place.model.places.component.JsonPlaceParser
import ua.place.model.places.component.NearPlacesUrlValidator
import ua.place.model.places.component.UriDecoderGooglePlaces
import ua.place.model.places.component.http.GooglePagesClient
import ua.place.model.places.entity.Location
import ua.place.model.places.entity.Response
import ua.place.model.places.enumer.StatusCodeEnum
import ua.place.model.places.exception.GoogleException
import ua.place.view.places.DataPlacesView

//SERVER
class PlacesController {

    def jsonParser = new JsonPlaceParser()
    def validator = new NearPlacesUrlValidator()
    def uriDecoder = new UriDecoderGooglePlaces()
    def googleClient = new GooglePagesClient()

    def run(url) {
        //если не прошли валидацию входных данных
        if (!validator.validate(url)) {
            new GoogleException(StatusCodeEnum.INVALID_REQUEST as String)
        }
        try {

            def pages = googleClient.pages(url)
            def parsedPlaces = []
            for (int i = 0; i < pages.size(); i++) {
                parsedPlaces += jsonParser.parsePages(new Location(
                        latitude: uriDecoder.getLatitude(url) as BigDecimal,
                        longitude: uriDecoder.getLontitude(url) as BigDecimal
                ), pages[0].results)
            }
           def formatedPlace=new FormatAndSort(places:  parsedPlaces)
                   .filter(uriDecoder.getFilterBy(url))
                   .sort(uriDecoder.getSortedBy(url))
                   .getPlaces()
            return new DataPlacesView(response: new Response(places: formatedPlace,
                    status: pages[pages.size() - 1].status,
                    next_page_token: pages[pages.size() - 1].next_page_token)
            )

        } catch (GoogleException ex) {
            return new DataPlacesView(response: new Response(places: [], status: ex.message))
        }
    }
}

