package com.iecas.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;

public class HadoopConfiguration {
    private static HadoopConfiguration hadoopConfiguration;
    private Configuration configuration = null;

    private HadoopConfiguration() throws IOException {
        System.out.println(DLProperty.getProperty("zooclientPort"));
        this.configuration = new Configuration();
        this.configuration.set("hbase.zookeeper.property.clientPort", DLProperty.getProperty("zooclientPort"));
        this.configuration.set("hbase.zookeeper.quorum", DLProperty.getProperty("zooquorum"));
        assert this.configuration != null;
    }

    public static Configuration getConfigurationInstance() throws IOException {
        if (hadoopConfiguration == null) {
            synchronized (HadoopConfiguration.class) {
                if (hadoopConfiguration == null) {
                    hadoopConfiguration = new HadoopConfiguration();
                }
            }
        }

        return hadoopConfiguration.configuration;
    }
}
