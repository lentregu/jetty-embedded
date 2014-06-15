package com.jetty.launcher.config;

import java.io.*;
import java.util.Properties;

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

//    public String get(String key) {
//
//        return properties.getProperty(key);
//
//    }

    public int serverPort() {

        String port = properties.getProperty("server.port", "8080");

        return Integer.parseInt(port);

    }

    public String serverLog() {

        return properties.getProperty("server.log", "/tmp/jetty-launcher-yyyy_mm_dd.request.log");
    }

    public String serverLogTimeZone() {

        return properties.getProperty("server.log.timezone", "UTC");
    }

    public String webAppWar() {

        return properties.getProperty("webapp.war");
    }

    public String webAppContextPath() {

        return properties.getProperty("webapp.contextPath","/");
    }

}