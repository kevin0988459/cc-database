package edu.cmu.cs.cloud;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Config {
    private String hostName;
    private String key;

    public Config(String fileName) throws IOException {
        Properties prop = new Properties();
        FileInputStream ip = new FileInputStream(fileName);
        prop.load(ip);
        this.hostName = prop.getProperty("hostName");
        this.key = prop.getProperty("key");
    }

    public String getHostName() {
        return hostName;
    }

    public String getKey() {
        return key;
    }
}

