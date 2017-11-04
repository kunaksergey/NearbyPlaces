package ua.place.model.places.component

import org.apache.http.client.utils.URLEncodedUtils
import ua.place.config.Config

/**
 * Created by sa on 04.11.17.
 */
class UriDecoderGooglePlaces {
    def getParams(url) {
        URLEncodedUtils.parse(new URI(url), "UTF-8")
    }

    def getLatitude(url) {
        getParams(url).find({ it.name == 'location' }).value.split(',')[0]
    }

    def getLontitude(url) {
        getParams(url).find({ it.name == 'location' }).value.split(',')[1]
    }

    def getRadius(url) {
        getParams(url).find({ it.name == 'radius' }).value
    }

    def getToken(url) {
        try{
        getParams(url).find({ it.name == 'pagetoken'?:null }).value
        }catch(NullPointerException ex){
            return ''
        }
    }

    def getSortedBy(url) {
        try{
            getParams(url).find({ it.name == 'sortedBy'?:null }).value
        }catch(NullPointerException ex){
            return ''
        }
    }

    def getFilterBy(url) {
        try{
            def filter=getParams(url).find({ it.name == 'filterBy'?:null }).value
            if(filter.indexOf(',')!=-1) return filter.split(',')
            []<<filter

        }catch(NullPointerException ex){
            return []
        }
    }

}
