package com.iecas.Configuration;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HbaseConnection {
    static Connection connection;

    public static Connection getHBASEConn() throws IOException {
        if (connection == null)
            connection = ConnectionFactory.createConnection(HadoopConfiguration.getConfigurationInstance());
        return connection;
    }

}
