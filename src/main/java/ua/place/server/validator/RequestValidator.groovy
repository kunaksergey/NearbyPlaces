package ua.place.server.validator

import ua.place.entity.transport.Request
import ua.place.server.enumer.StatusCodeEnum
import ua.place.server.exception.BadRequestException

/**
 * Created by sa on 02.11.17.
 */
class RequestValidator {
    def validate(request){
        request instanceof Request
        try {
            def latitude = new BigDecimal(request.location.latitude as BigDecimal)
            def longitude = new BigDecimal(request.location.longitude as BigDecimal)

            def radius = new Integer(request.radius as Integer)
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

