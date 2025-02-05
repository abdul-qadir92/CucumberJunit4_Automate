package StepDefinitions;

import com.pages.bsHome;
import com.pages.bsOffers;
import com.qa.factory.DriverFactory;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import java.util.Iterator;
import java.util.List;


public class OffersSteps {
    public WebDriver driver;
    private bsHome bshome = new bsHome(DriverFactory.getDriver());
    private bsOffers bsoffer = new bsOffers(DriverFactory.getDriver());
    Scenario sc;

    @Before
    public void getScenario(Scenario scenario){
        this.sc = scenario;
    }
    @And("User Clicks on Offers")
    public void user_clicks_on_offers() {
        bshome.openOffers();
    }
    @Then("User can see offers list for Mumbai location")
    public void user_can_see_offers_list_for_mumbai_location() {
       List<String> offerDetails = bsoffer.getOffers();
       if(offerDetails.isEmpty()){
           Assert.fail("No offers found for your location");
       }else{
           Iterator t = offerDetails.iterator();
           //System.out.println("Offers for your location:");
           sc.log("Offers for your location:");
           while(t.hasNext()){
               //System.out.println(t.next());
               sc.log((String) t.next());
           }
       }
    }
}
