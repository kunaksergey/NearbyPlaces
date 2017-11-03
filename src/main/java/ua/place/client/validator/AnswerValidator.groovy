package ua.place.client.validator

import ua.place.client.exception.BadAnswerException
import ua.place.client.enumer.StatusCodeEnum

/**
 * Created by sa on 02.11.17.
 */
class AnswerValidator {
    def validate(answer) {
        answer instanceof Map
        try {
            def latitude = new BigDecimal(answer['latitude'] as BigDecimal)
            def longitude = new BigDecimal(answer['longitude']as BigDecimal)

            def radius = new Integer(answer['radius'] as Integer)
            if (latitude < -90 || latitude > 90) throw new BadAnswerException(StatusCodeEnum.INVALID_DATA as String)
            if (longitude < -180 || longitude > 180) throw new BadAnswerException(StatusCodeEnum.INVALID_DATA as String)
            if (radius < 0 || radius > 50000) throw new BadAnswerException(StatusCodeEnum.INVALID_DATA as String)
            return true
        }catch (NumberFormatException ex) {
            println "Bad inner data"
            return false
        }catch (BadAnswerException ex) {
            println "Bad inner data"
           return false
        }
    }
}
