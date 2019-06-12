package com.iecas;

import java.net.URL;

import com.iecas.utils.HbaseUtils;
import org.dom4j.Document;
import com.iecas.utils.XMLP;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "file:///C:\\Users\\MILK\\Desktop\\wmts.xml";
        //HbaseConnection.getHBASEConn();
        if (args.length == 0) {
            System.out.println("Usage:\t--getLayers [-f xml_url]\n\t--deleteLayer layername start end\n\t--scanRows layername start end\"");
        } else if (args[0].equals("--getLayers")) {
            if (args.length == 3) {
                if (args[1].equals("-f"))
                    url = args[2];
            }
            Document document = XMLP.parse(new URL(url));
            XMLP.getLayerNames(document);
        } else if (args[0].equals("--scanRows")) {
            String layerName = args[1];
            Document document = XMLP.parse(new URL(url));
            if (XMLP.getLayerNames(document).containsValue(layerName)) {
                int start = Integer.valueOf(args[args.length - 2]);
                int end = Integer.valueOf(args[args.length - 1]);
                for (int z = start; z <= end; ++z) {
                    //QuadTreeUtil.xyz2QuadTreeCodes(XMLP.getLayerInfo(document, "EPSG:4326", layerName, z), z,layerName);
                    String prefix = String.format("%02d", z);
                    HbaseUtils.scanRows("hbase_tile_table", layerName + prefix);
                }
            } else {
                System.out.println("ERROR!! Layer " + layerName + " not exist!!!");
            }

        } else if (args[0].equals("--deleteLayer")) {
            String layerName = args[1];
            if (args.length < 4)
                System.out.println("Please identify the start and end !");
            else {
                int start = Integer.valueOf(args[args.length - 2]);
                int end = Integer.valueOf(args[args.length - 1]);
                for (int z = start; z <= end; ++z) {
                    //QuadTreeUtil.xyz2QuadTreeCodes(XMLP.getLayerInfo(document, "EPSG:4326", layerName, z), z,layerName);
                    String prefix = String.format("%02d", z);
                    HbaseUtils.deleteRows("hbase_tile_table", layerName + prefix);
                }
            }
        }

    }
}