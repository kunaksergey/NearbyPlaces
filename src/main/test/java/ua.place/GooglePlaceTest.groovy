package ua.place

import groovy.json.JsonSlurper
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by sa on 29.10.17.
 */
class GooglePlaceTest {
    GooglePlace googlePlace
    final FILE_NAME = "testData"
    def json

    @Before
    void init() {
        googlePlace = new GooglePlace(latitude: 48.45925, longitude: 35.04497)
        def file = new File(FILE_NAME)
        json = new JsonSlurper().parseText(file.text)
    }

    @Test
    void testEmptyLimit() {
        googlePlace.result = []
        Assert.assertEquals(0, googlePlace.limit(1).loadResult().size())
    }

    @Test
    void testEqualLimit() {
        googlePlace.result = json.results
        Assert.assertEquals(3, googlePlace.limit(3).loadResult().size())

    }

    @Test
    void testLessThenLimit() {
        googlePlace.result = json.results
        Assert.assertEquals(20, googlePlace.limit(50).loadResult().size())
    }

    @Test
    void testSortedByFieldName() {
        def result = []
        json.results.each { result << googlePlace.parsePlace(it) }
        googlePlace.result = result
        def place = googlePlace.sortedByField("name").limit(1).loadResult()[0]
        Assert.assertEquals("eb0dfc2a20595b7c5a731193493ee6ae1f05b08c", place.id )
    }

    @Test
    void testSortedByFieldDistance() {
        def result = []
        json.results.each { result << googlePlace.parsePlace(it) }
        googlePlace.result = result
        def place = googlePlace.sortedByField("distance").limit(1).loadResult()[0]
        Assert.assertEquals("972519b8fe24b7944f640a65677ba7f7239e333c", place.id )
    }

    @Test
    void testFilterByTypeGym() {
        def result = []
        json.results.each { result << googlePlace.parsePlace(it) }
        googlePlace.result = result
        def place = googlePlace.filterByType("gym").loadResult()[0]
        Assert.assertEquals("33cb4a786a79da2033c57dda63bb99e11bbcba51", place.id)
    }

    @Test
    void testFilterByTypeHealth() {
        def result = []
        json.results.each { result << googlePlace.parsePlace(it) }
        googlePlace.result = result
        def places = googlePlace.filterByType("health").loadResult()
        Assert.assertEquals(2, places.size())
    }
}
