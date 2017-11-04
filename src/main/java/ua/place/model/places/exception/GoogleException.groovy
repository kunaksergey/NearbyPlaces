package ua.place.model.places.exception

import org.apache.http.client.ClientProtocolException

class GoogleException extends ClientProtocolException{
    GoogleException(String message) {
        super(message)
    }

    GoogleException(Throwable cause) {
        super(cause)
    }
}
