package com.iecas.Configuration;

import java.io.IOException;
import java.util.Properties;
import org.apache.hadoop.conf.Configuration;

public class HadoopConfiguration {
    private static HadoopConfiguration hadoopConfiguration;
    private Configuration configuration = null;

    private HadoopConfiguration() {
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("setting.properties"));
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        this.configuration = new Configuration();
        this.configuration.set("hbase.zookeeper.property.clientPort", properties.getProperty("zooclientPort"));
        this.configuration.set("hbase.zookeeper.quorum", properties.getProperty("zooquorum"));
        //this.configuration.addResource("resources/core-site.xml");
        //this.configuration.addResource("resources/hdfs-site.xml");

        assert this.configuration != null;

    }

    public static Configuration getConfigurationInstance() {
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
