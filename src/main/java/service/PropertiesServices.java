package service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class  PropertiesServices {


    /**
     * Mi prende la properties dal file delle properties
     * @param name
     * @return
     * @throws IOException
     */
    public static String getProperties(String name) throws IOException {

        String output = "";

        Properties prop = new Properties();
        String propFileName = "config.properties";


        InputStream inputStream = PropertiesServices.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }



        return prop.getProperty(name);
    }






}
