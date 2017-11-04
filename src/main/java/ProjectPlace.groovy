import ua.place.config.Config
import ua.place.controller.places.PlacesController

def url
url= Config.BASE_URL+Config.NEAR_BY_SEARCH_URI+"?filterBy=finance&sortedBy=name&location=48.512253,35.084477&radius=500&key=AIzaSyBpLk-GrkZy8N599XaP9RTsBl-kGNr2Fpg"
def placeController=new PlacesController()
def view=placeController.run(url)
view.print()
sleep(Config.PAUSE)
view=placeController.run(url+"&pagetoken="+view.next_page_token())
view.print()



