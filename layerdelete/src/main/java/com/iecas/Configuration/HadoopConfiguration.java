package com.iecas.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;

public class HadoopConfiguration {
    private static HadoopConfiguration hadoopConfiguration;
    private Configuration configuration = null;

    private HadoopConfiguration() throws FileNotFoundException {
        Properties properties = new Properties();
        FileReader fr = new FileReader("setting.properties");

        try {
            //properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("setting.properties"));
            properties.load(fr);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
        System.out.println(properties.getProperty("zooclientPort"));
        this.configuration = new Configuration();
        this.configuration.set("hbase.zookeeper.property.clientPort", properties.getProperty("zooclientPort"));
        this.configuration.set("hbase.zookeeper.quorum", properties.getProperty("zooquorum"));
        //this.configuration.addResource("resources/core-site.xml");
        //this.configuration.addResource("resources/hdfs-site.xml");

        assert this.configuration != null;

    }

    public static Configuration getConfigurationInstance() throws FileNotFoundException {
        if (hadoopConfiguration == null) {
            synchronized(HadoopConfiguration.class) {
                if (hadoopConfiguration == null) {
                    hadoopConfiguration = new HadoopConfiguration();
                }
            }
        }

        return hadoopConfiguration.configuration;
    }
}
