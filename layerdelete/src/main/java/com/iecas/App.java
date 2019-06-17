package com.iecas;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iecas.Executor.DelExecutor;
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
            for (Map.Entry entry : XMLP.getLayerNames(document).entrySet()) {
                System.out.println(entry.getValue());
            }
            System.out.println();

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
        } else if (args[0].equals("--tileLayer")) {
            String layerName = args[1];
            Document document = XMLP.parse(new URL(url));
            if (XMLP.getLayerNames(document).containsValue(layerName)) {
                if (args.length < 4)
                    System.out.println("Please identify the start and end !");
                else {
                    String style = XMLP.getLayerStyle(document,layerName);
                    int start = Integer.valueOf(args[args.length - 2]);
                    int end = Integer.valueOf(args[args.length - 1]);
                    ExecutorService service = Executors.newFixedThreadPool(8);
                    for (int z = start; z <= end; ++z) {
                        int[] xyz = XMLP.getLayerInfo(document, "EPSG:4326", layerName, z);
                        int minR = xyz[0];
                        int maxR = xyz[1];
                        int minC = xyz[2];
                        int maxC = xyz[3];
                        for (int x = minR; x < maxR; ++x) {
                            for (int y = minC; y < maxC; ++y) {
                                service.execute(new DelExecutor(layerName, x,y, z, style));
                            }
                        }
                        System.out.println("submit finish");
                        service.shutdown();
                    }
                }
            } else {
                System.out.println("ERROR!! Layer " + layerName + " not exist!!!");
            }
        }

    }
}