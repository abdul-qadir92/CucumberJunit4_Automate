package StepDefinitions;

import com.qa.factory.DriverFactory;
import com.qa.util.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;
import java.util.Properties;

public class ApplicationHooks {
    private DriverFactory driverfactory;
    public WebDriver driver;
    private ConfigReader config;
    Properties prop;
    Scenario scenario;

    @Before(order=0)
    public void getProperty(){
        config = new ConfigReader();
        prop = config.init_prop();
    }
    @Before(order=1)
    public void beforeScenario(Scenario scenario){
        this.scenario = scenario;
    }
    @Before(order=2)
    public void launchBrowser() throws Exception {
        driverfactory = new DriverFactory();
        String browser = prop.getProperty("browser");
        driver = driverfactory.init_driver(browser);
    }
    @After(order=0)
    public void tearDown(){
        driver.quit();
    }
    @After(order=1)
    public void screenshot(Scenario sc){
        String screenshotName = sc.getName().replaceAll(" ","_");
        byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        sc.attach(sourcePath,"image/png",screenshotName);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\" "+       sc.getName().toString()   +" \" }}");
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + sc.getStatus().toString() + "\", \"reason\": \"" + "\"}}");


    }

}
