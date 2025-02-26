package StepDefinitions;

import com.pages.bsCheckout;
import com.pages.bsConfirmation;
import com.pages.bsHome;
import com.pages.bsSignin;
import com.qa.factory.DriverFactory;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class E2ESteps {

    public WebDriver driver;
    private bsHome bshome = new bsHome(DriverFactory.getDriver());
    private bsSignin bssignin = new bsSignin(DriverFactory.getDriver());
    private bsCheckout bschecokout = new bsCheckout(DriverFactory.getDriver());
    private bsConfirmation bsconfirm = new bsConfirmation(DriverFactory.getDriver());
    Scenario sc;

    @Before
    public void getScenario(Scenario scenario){
        this.sc = scenario;
    }
    @Given("User is on home page")
    public void user_is_on_home_page() {
        DriverFactory.getDriver().get("https://bstackdemo.com/");
    }
    @When("User clicks on sign in link")
    public void user_clicks_on_sign_in_link() {
        bshome.userSignIn();
    }
    @And("User enters {string} and {string} and clicks on sign in")
    public void SigninDetails(String user, String pass) throws InterruptedException {
        bssignin.userCredentials(user,pass);
    }
    @And("{string} is signed in to the App")
    public void user_is_signed_in_to_the_app(String user) {
        Boolean success = bshome.verifySignin();
        if(success) System.out.println(user + " signed in successfully");
        else{
            Assert.fail(user+" login failed");
        }
    }
    @And("User clicks on Apple in Vendor filter")
    public void user_clicks_on_apple_in_vendor_filter() {
        bshome.searchProduct();
    }
    @And("User sorts the search result by Price: Low to High")
    public void sorts_the_search_result_by_price_low_to_high() throws InterruptedException {
        bshome.sortResult();
    }
    @And("User can see the list of Apple products with Price in ascending order")
    public void user_can_see_the_list_of_apple_products_with_price_in_ascending_order() {
        Map<String, String> iphonelist = bshome.searchResult();
        //System.out.println("Details of Apple Products:");
        sc.log("Details of Apple Products:");
        //iphonelist.forEach((k,v)->System.out.println(k+" $"+v));
        iphonelist.forEach((k,v)->sc.log(k+" $"+v));
    }
    @And("User clicks on Add to cart for the iPhone XR and clicks on CHECKOUT")
    public void user_clicks_on_add_to_cart_for_the_i_phone_xr_and_clicks_on_checkout() {
        bshome.AddtoCart();
    }
    @And("User enters {string},{string},{string},{string},{int} and clicks SUBMIT")
    public void user_enters_and_clicks_submit(String first, String last, String add, String state, Integer pin) {
        bschecokout.fillAddress(first,last,add,state,pin);
    }
    @Then("User can see the order id for the product")
    public void user_can_see_the_order_id_for_the_product() {
        String orderid = bsconfirm.verifyPurchase();
        Assert.assertNotEquals("",orderid,"Order ID confirmation: "+orderid);
        sc.log("Order ID confirmation: "+orderid);
    }

}
