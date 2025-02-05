package com.qa.factory;

import com.browserstack.local.Local;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.FileReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DriverFactory {

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private Local l;
    HashMap<String, String> browserstackOptions = new HashMap<>();
    static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YY hh.mm");
    public static String buildname = sdf.format(new Date());

    /**
     * This method is used to initialize the threadlocal driver from the config
     * @param browser
     * @return
     */
    public WebDriver init_driver(String browser) throws Exception {

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().clearDriverCache().setup();
            tlDriver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().clearDriverCache().setup();
            tlDriver.set(new FirefoxDriver());
        } else if(browser.equalsIgnoreCase("safari")) {
            WebDriverManager.safaridriver().clearDriverCache().setup();
            tlDriver.set(new SafariDriver());
        } else if (browser.equalsIgnoreCase("remote")) {
            JSONParser parser = new JSONParser();
            String config_file = "single.conf.json";
            JSONObject config;
            JSONArray envs = null;
            if (System.getProperty("caps-type") != null && System.getProperty("caps-type").toLowerCase().contains("parallel"))
                config_file = "parallel.conf.json";
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/browserstack/" + config_file));
            envs = (JSONArray) config.get("environments");
            Map<String, String> env = null;
            for (Object item : envs) {
                env = (Map<String, String>) item;
            }
            DesiredCapabilities capabilities = new DesiredCapabilities();
            Map<String, String> envCapabilities = (Map<String, String>) env;
            Iterator it = envCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (pair.getKey().toString().toLowerCase().contains("envoptions")) {
                    browserstackOptions = (HashMap<String, String>) pair.getValue();
                } else
                    capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
            Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
            it = commonCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                browserstackOptions.put(pair.getKey().toString(), pair.getValue().toString());
            }
            browserstackOptions.put("buildName",browserstackOptions.get("projectName") + "-" + buildname);
            if (System.getenv("BROWSERSTACK_BUILD_NAME") != null) {
                browserstackOptions.put("build", System.getenv("BROWSERSTACK_BUILD_NAME").toString());
            }

            String username = System.getenv("BROWSERSTACK_USERNAME");
            if (username == null) {
                username = (String) config.get("user");
            }

            String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
            if (accessKey == null) {
                accessKey = (String) config.get("key");
            }

            if (capabilities.getCapability("browserstack.local") != null
                    && capabilities.getCapability("browserstack.local") == "true") {
                l = new Local();
                Map<String, String> options = new HashMap<String, String>();
                options.put("key", accessKey);
                l.start(options);
            }
            capabilities.setCapability("bstack:options", browserstackOptions);
            tlDriver.set(new RemoteWebDriver(
                    new URL("http://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub"), capabilities));

        }
        //region WindowCustomize
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if(browserstackOptions.get("realMobile")==null)
            getDriver().manage().window().maximize();
        //endregion
        return getDriver();
    }

    /**
     * This is used to get the driver with ThreadLocal
     * @return
     */
    public static synchronized WebDriver getDriver(){
        return tlDriver.get();
    }
}
