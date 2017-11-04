package ua.place.model.places.component


import ua.place.model.places.enumer.StatusCodeEnum
import ua.place.model.places.exception.BadRequestException

/**
 * Created by sa on 02.11.17.
 */
class NearPlacesUrlValidator {
    def validate(url){
        try {
            def uriDecoder=new UriDecoderGooglePlaces()
            def latitude = new BigDecimal(uriDecoder.getLatitude(url) as BigDecimal)
            def longitude = new BigDecimal(uriDecoder.getLontitude(url) as BigDecimal)

            def radius = new Integer(uriDecoder.getRadius(url) as Integer)
            if (latitude < -90 || latitude > 90) throw new BadRequestException(StatusCodeEnum.INVALID_REQUEST as String)
            if (longitude < -180 || longitude > 180) throw new BadRequestException(StatusCodeEnum.INVALID_REQUEST as String)
            if (radius < 0 || radius > 50000) throw new BadRequestException(StatusCodeEnum.INVALID_REQUEST as String)
            return true
        } catch (BadRequestException ex) {
            return false
        } catch (NumberFormatException ex) {
            return false
        }
    }

}

