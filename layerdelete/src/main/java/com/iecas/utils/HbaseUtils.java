package com.iecas.utils;

import com.iecas.Configuration.HadoopConfiguration;
import com.iecas.Configuration.HbaseConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseUtils {
    private static final Log log = LogFactory.getLog(HbaseUtils.class);



    /*public static Result getResult(String tableName, String rowKey) throws Exception {
        //Get get=new Get(Bytes.toBytes(rowKey));
        Scan scan = new Scan();
        scan.addColumn("".getBytes(), "".getBytes());
        scan.setRowPrefixFilter(Bytes.toBytes("row"));
        Table table = getTable(HbaseConnection.getHBASEConn(), tableName);
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                System.out.println(r.getRow().length);
            }
        } finally {
            rs.close();
        }

        return new Result();
    }*/

    public static Table getTable(Connection connection, String tableName) throws Exception {
        TableName tableName1 = TableName.valueOf(tableName);
        log.info("----------------" + tableName + "-----------------");
        Admin admin = HbaseConnection.getHBASEConn().getAdmin();
        if (!admin.tableExists(tableName1)) {
            log.error("GET TABLE FAILED: <<" + tableName + ">> NOT EXIST!!");
        }

        return connection.getTable(tableName1);
    }

    public static Result getResult(String tableName, String row) throws Exception {
        Table table = getTable(HbaseConnection.getHBASEConn(), tableName);
        Get get = new Get(Bytes.toBytes(row));
        Result result = table.get(get);

        return result;
    }
}
