import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class main {
    public static String getPinYin(String src) throws BadHanyuPinyinOutputFormatCombination {
        char[] hz = null;
        hz = src.toCharArray();//该方法的作用是返回一个字符数组，该字符数组中存放了当前字符串中的所有字符
        String[] py = new String[hz.length];//该数组用来存储
        //设置汉子拼音输出的格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String pys = ""; //存放拼音字符串
        int len = hz.length;

        try {
            for (int i = 0; i < len ; i++ ){
                //先判断是否为汉字字符
                if(Character.toString(hz[i]).matches("[\\u4E00-\\u9FA5]+")){
                    //将汉字的几种全拼都存到py数组中
                    py = PinyinHelper.toHanyuPinyinStringArray(hz[i],format);
                    //取出改汉字全拼的第一种读音，并存放到字符串pys后
                    pys += py[0];
                }else{
                    //如果不是汉字字符，间接取出字符并连接到 pys 后
                    pys += Character.toString(hz[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e){
            e.printStackTrace();
        }
        return pys;
    }

    public static String getPinYinHeadChar(String str){
        String convert = "";
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            //提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null){
                convert += pinyinArray[0].charAt(0);
            }else{
                convert += word;
            }
        }
        return convert.toUpperCase();
    }


    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, SQLException, BadHanyuPinyinOutputFormatCombination, InterruptedException {
        //GeoServer的连接配置
        String url = "http://localhost:8080/geoserver" ;
        String username = "admin" ;
        String passwd = "geoserver" ;

        //postgis连接配置
        String postgisHost = "192.168.2.132" ;
        int postgisPort = 5432 ;
        String postgisUser = "postgres" ;
        String postgisPassword = "123456" ;
        String postgisDatabase = "shpfile" ;

        String ws = "test" ;     //待创建和发布图层的工作区名称workspace
        String store_name = "shpfile" ; //待创建和发布图层的数据存储名称store
        String table_name = "wafangdianshi" ; // 数据库要发布的表名称,后面图层名称和表名保持一致
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://" + postgisHost + ":" + postgisPort + "/" + postgisDatabase, postgisUser, postgisPassword);
        ResultSet rs = con.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
        URL u = new URL(url);
        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
        GeoServerRESTPublisher publisher = manager.getPublisher() ;
        List<String> workspaces = manager.getReader().getWorkspaceNames();
        if(!workspaces.contains(ws)){
            boolean createws = publisher.createWorkspace(ws);
            System.out.println("create ws : " + createws);
        }else {
            System.out.println("workspace已经存在了,ws :" + ws);
        }
        //判断数据存储（datastore）是否已经存在，不存在则创建
        RESTDataStore restStore = manager.getReader().getDatastore(ws, store_name);
        if(restStore == null){
            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(store_name);
            store.setHost(postgisHost);//设置url
            store.setPort(postgisPort);//设置端口
            store.setUser(postgisUser);// 数据库的用户名
            store.setPassword(postgisPassword);// 数据库的密码
            store.setDatabase(postgisDatabase);// 那个数据库;
            store.setSchema("public"); //当前先默认使用public这个schema
            store.setConnectionTimeout(20);// 超时设置
            //store.setName(schema);
            store.setMaxConnections(20); // 最大连接数
            store.setMinConnections(1);     // 最小连接数
            store.setExposePrimaryKeys(true);
            boolean createStore = manager.getStoreManager().create(ws, store);
            System.out.println("create store : " + createStore);
        } else {
            System.out.println("数据存储已经存在了,store:" + store_name);
        }

        while (rs.next()) {
            //System.out.println(rs.getString(3));
            table_name = rs.getString(3);
            table_name = table_name.replace('（','(').replace('）',')').replaceAll(" ","");
            table_name = getPinYinHeadChar(table_name);
            System.out.println(table_name);
            //判断图层是否已经存在，不存在则创建并发布
            RESTLayer layer = manager.getReader().getLayer(ws, table_name);
            if(layer == null){
                GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
                pds.setTitle(rs.getString(3));
                pds.setName(table_name);
                pds.setSRS("EPSG:4326");
                GSLayerEncoder layerEncoder = new GSLayerEncoder();
                boolean publish = manager.getPublisher().publishDBLayer(ws, store_name,  pds, layerEncoder);
                System.out.println("publish : " + publish);
            }else {
                System.out.println("表已经发布过了,table:" + table_name);
            }
        }

    }
}