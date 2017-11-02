package ua.place.server.exception

/**
 * Created by sa on 02.11.17.
 */
class BadRequestException  extends Exception{
    BadRequestException(String message) {
        super(message)
    }
}
