package com.jetty.launcher;

import java.io.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: gfr
 * Date: 10/06/14
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class ConfigReader {

    private Properties properties;
    private String configFile = null;
    private static ConfigReader instance = null;

    private ConfigReader (String configFile) {
        this.configFile = configFile;
        this.properties = new Properties();
    }

    public static ConfigReader getInstance(String configFile) {

        if (instance == null) {
            instance = new ConfigReader(configFile);
        }

        return instance;
    }

    public void loadProperties() throws IOException {

        InputStream configFileStream = null;

        try {

            //configFileStream =ServletContext.class.getClassLoader().getResourceAsStream(configFile);
            configFileStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
           // printStream(configFileStream);

            properties.load(configFileStream);

        } finally {

            configFileStream.close();

        }

    }

    private void printStream(InputStream inputStream) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 1);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public String get(String key) {

        return properties.getProperty(key);

    }

/*

    public static void main(String[] args) throws IOException {

        PropertiesReader propsReader = new PropertiesReader();

        propsReader.loadProperties("config.properties");// load properties

        String content = propsReader.get("test.address");// load value

        System.out.println(content);// print it

    }

    */

}