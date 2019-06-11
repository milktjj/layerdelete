package com.iecas;

import java.net.MalformedURLException;
import java.net.URL;

import com.iecas.Configuration.HbaseConnection;
import com.iecas.utils.HbaseUtils;
import com.iecas.utils.QuadTreeUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import com.iecas.utils.XMLP;

import javax.xml.parsers.DocumentBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "file:///C:\\Users\\MILK\\Desktop\\wmts.xml";
        //HbaseConnection.getHBASEConn();
        if(args.length == 0) {
            System.out.println("Usage:\t--getLayers [-f xml_url]\n\t--deleteLayer layername start end\n\t--scanLayer layername start end\"");
        }else if (args[0].equals("--getLayers")) {
            if (args.length == 3) {
                if (args[1].equals("-f"))
                    url = args[2];
            }
            Document document = XMLP.parse(new URL(url));
            XMLP.getLayerNames(document);
        }else if (args[0].equals("--scanRows")) {
            String layerName = args[1];
            int start = Integer.valueOf(args[args.length-2]);
            int end = Integer.valueOf(args[args.length-1]);
            for (int z = start; z <= end; ++z) {
                //QuadTreeUtil.xyz2QuadTreeCodes(XMLP.getLayerInfo(document, "EPSG:4326", layerName, z), z,layerName);
                String prefix = String.format("%02d", z);
                HbaseUtils.scanRows("hbase_tile_table", layerName+prefix);
            }
        }else if (args[0].equals("--deleteLayer")) {
            String layerName = args[1];
            int start = Integer.valueOf(args[args.length-2]);
            int end = Integer.valueOf(args[args.length-1]);
            for (int z = start; z <= end; ++z) {
                //QuadTreeUtil.xyz2QuadTreeCodes(XMLP.getLayerInfo(document, "EPSG:4326", layerName, z), z,layerName);
                String prefix = String.format("%02d", z);
                HbaseUtils.deleteRows("hbase_tile_table", layerName+prefix);
            }
        }

    }
}