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
import java.util.ArrayList;
import java.util.List;

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
        Get get = new Get(row.getBytes());
        Result result = table.get(get);
        if (result == null)
            log.error("Key " + row + " not EXIST!!");
        return result;
    }

    public static void scanRows(String tableName, String prefixRow) throws Exception {
        Table table = getTable(HbaseConnection.getHBASEConn(), tableName);

        Scan scan = new Scan();
        scan.setRowPrefixFilter(prefixRow.getBytes());
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                //System.out.println(r.getRow().length);
                System.out.println(new String(r.getRow()));
            }
        } finally {
            rs.close();
        }

    }

    public static void deleteRows(String tableName, String prefixRow) throws Exception {
        Table table = getTable(HbaseConnection.getHBASEConn(), tableName);
        List<Delete> deletes = new ArrayList<Delete>();
        Scan scan = new Scan();
        scan.setRowPrefixFilter(prefixRow.getBytes());
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                System.out.println(new String(r.getRow()));
                deletes.add(new Delete(r.getRow()));
                if (deletes.size() > 100000) {
                    table.delete(deletes);
                    deletes.clear();
                }
            }
        } finally {
            rs.close();
        }
        table.delete(deletes);
    }

}
