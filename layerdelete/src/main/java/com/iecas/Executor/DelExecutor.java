package com.iecas.Executor;

import com.iecas.Configuration.DLProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DelExecutor extends Thread {
    private String layername;
    private int z;
    private int x;
    private int y;
    private String matrix = DLProperty.getProperty("matrix");
    private String port = DLProperty.getProperty("geoserverPort");
    private String geturl = String.format("http://geoserver:%s/geoserver/gwc/rest/wmts/%s/%s/%s/%s/%d/%d?format=image/png",port);
    private String style = "raster";

    public DelExecutor(String layername, int x, int y, int z, String style) throws IOException {
        this.layername = layername;
        this.x = x;
        this.y = y;
        this.z = z;
        if(style.length()>0)
            this.style = style;
    }

    public void run() {
        try {
            URL url = new URL(String.format(geturl, layername, style,matrix, matrix + ':' + z, x, y));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            br.close();
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
