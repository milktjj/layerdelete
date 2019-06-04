import org.dom4j.Document;
import org.dom4j.DocumentException;
import utils.XMLP;

import javax.xml.parsers.DocumentBuilder;
import java.net.MalformedURLException;
import java.net.URL;

public class test {
    public static void main(String[] args) throws MalformedURLException, DocumentException {
        long[] xyz = new long[3];
        xyz[0] = 2L;
        xyz[1] = 23L;
        xyz[2] = 10L;
        System.out.println(utils.QuadTreeUtil.xyz2QuadTreeCode(xyz));
        Document document = XMLP.parse(new URL("file:///C:\\Users\\MILK\\Desktop\\wmts.xml"));
        //XMLP.getLayerNames(document);
        System.out.println(XMLP.getLayerInfo(document, "EPSG:4326", "DESP:全国SRTM拼接",13)[1]);
    }
}
