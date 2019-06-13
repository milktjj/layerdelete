package com.iecas.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLP {
    public static Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);
        return document;
    }

    public static int[] getLayerInfo(Document document, String matrix,String layerName,int z) throws DocumentException {

        Element root = document.getRootElement();
        int[] limit = new int[4];
        for (Iterator<Element> it = root.element("Contents").elementIterator(); it.hasNext(); ) {
            Element foo = it.next();
            if (foo.getName().equals("Layer")) {
                // iterate through attributes of root
                if (foo.element("Identifier").getStringValue().equals(layerName)) {
                    for (Iterator<Element> matruxSet_it = foo.elementIterator("TileMatrixSetLink"); matruxSet_it.hasNext(); ) {
                        Element EPSG = matruxSet_it.next();
                        if (EPSG.element("TileMatrixSet").getStringValue().equals(matrix)) {
                            for (Iterator<Element> EPSG_4326_it = EPSG.element("TileMatrixSetLimits").elementIterator(); EPSG_4326_it.hasNext(); ) {
                                Element element = EPSG_4326_it.next();
                                // do something
                                if (element.getName().equals("TileMatrixLimits")) {
                                    if (element.element("TileMatrix").getStringValue().equals(matrix + ":" + z)) {
                                        limit[0] = Integer.parseInt(element.element("MinTileRow").getStringValue());
                                        limit[1] = Integer.parseInt(element.element("MaxTileRow").getStringValue());
                                        limit[2] = Integer.parseInt(element.element("MinTileCol").getStringValue());
                                        limit[3] = Integer.parseInt(element.element("MaxTileCol").getStringValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return limit;

    }

    public static Map<Integer, String> getLayerNames(Document document) throws DocumentException {

        Element root = document.getRootElement();
        Map layermap = new HashMap();
        int i = 0;
        for (Iterator<Element> it = root.element("Contents").elementIterator(); it.hasNext(); ) {
            Element foo = it.next();
            if (foo.getName().equals("Layer")) {
                // iterate through attributes of root
                layermap.put(++i,foo.element("Identifier").getStringValue());
            }
        }

        return layermap;
    }
}
