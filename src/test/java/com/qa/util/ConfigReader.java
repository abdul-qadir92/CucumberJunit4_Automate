package com.qa.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties prop;

    /**
     * This will load the properties from config
     * @return Properties object prop
     */
    public Properties init_prop(){
        prop = new Properties();
        try {
            FileInputStream fs = new FileInputStream("src/test/resources/config/config.properties");
            prop.load(fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
