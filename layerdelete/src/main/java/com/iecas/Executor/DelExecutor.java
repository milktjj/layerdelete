package com.iecas.Executor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DelExecutor extends Thread {
    private String layername;
    private int z;
    private int x;
    private int y;
    private String matrix = "EPSG:4326";
    private String geturl = "http://geoserver:8080/geoserver/gwc/rest/wmts/%s/%s/%s/%s/%d/%d?format=image/png";
    private String style = "raster";

    public DelExecutor(String layername, int x, int y, int z, String style) {
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
            connection.setDoOutput(true); // 设置该连接是可以输出的
            connection.setRequestMethod("GET"); // 设置请求方式
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            br.close();
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
