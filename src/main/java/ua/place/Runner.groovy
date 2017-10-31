package ua.place

import groovyx.net.http.HTTPBuilder
import org.apache.tools.ant.filters.SortFilter
import ua.place.config.Config
import ua.place.entity.InсomeData
import ua.place.entity.UserAnswer
import ua.place.manipulation.FilterSortChainPlace
import ua.place.parser.Parser
import ua.place.http.GooglePagesClient
import ua.place.service.HandlerRecipient


class Runner {

    def run(incomeDate){
        assert incomeDate instanceof InсomeData
        def pageRecipient = new GooglePagesClient().requestPages(incomeDate)
        //pageRecipient.requestPages(incomeDate)
        //def handlerRecipient = new HandlerRecipient()
        def parsedList=new Parser().parsePages(incomeDate.location,pageRecipient)
        def result=new FilterSortChainPlace(places:parsedList).filter(incomeDate.filterBy)
       // new UserAnswer(list,)

 }
}
