package ua.place.exception

import org.apache.http.client.ClientProtocolException

class GoogleException extends ClientProtocolException{
    GoogleException(String message) {
        super(message)
    }
}