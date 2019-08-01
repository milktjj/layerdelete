package com.iecas.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import java.util.ArrayList;
import java.util.List;

public class QuadTreeUtil {
    private static final Log log = LogFactory.getLog(QuadTreeUtil.class);

    public QuadTreeUtil() {
    }

    public static String xyz2QuadTreeCode_1(long[] xyz) {
        long numOfTier = xyz[2];
        Long x = xyz[0];
        Long y = xyz[1];
        Long z = xyz[2];
        StringBuilder sb = new StringBuilder();
        String str = String.valueOf(z);
        if (str.length() == 1) {
            sb.append(0);
        }

        sb.append(z);

        for (int i = 0; (long) i <= numOfTier; ++i) {
            long mid = (long) Math.pow(2.0D, (double) (numOfTier - (long) i)) - 1L;
            if (x > mid && y > mid) {
                sb.append(2);
            } else if (x > mid && y <= mid) {
                sb.append(3);
            } else if (x <= mid && y > mid) {
                sb.append(1);
            } else {
                sb.append(0);
            }

            x = x % (long) Math.pow(2.0D, (double) (numOfTier - (long) i));
            y = y % (long) Math.pow(2.0D, (double) (numOfTier - (long) i));
        }

        return sb.toString();
    }

    public static String xyz2QuadTreeCode(int[] xyz, Document document, String layername, String matrix) throws DocumentException {
        int x = xyz[0];
        int y = xyz[1];
        int z = xyz[2];

        int[] t1 = XMLP.getLayerInfo(document, matrix, layername, z);
        int[] t2 = XMLP.getLayerInfo(document, matrix, layername, z+1);
        x = t2[3] - x + t1[2];

        long[] xyz_l = new long[3];
        xyz_l[0] = x;
        xyz_l[1] = y;
        xyz_l[2] = z;
        return xyz2QuadTreeCode_1(xyz_l);
    }

    public static void xyz2QuadTreeCodes(int[] RC, int z) throws Exception {
        long[] xyz = new long[3];
        xyz[2] = z;
        //x是下一层的列号的倒叙  y是行号    如
        //下一层为  xmax = 20  xmin = 10
        //这层为    xmax = 10  xmin 5
        //x顺序为    20 19 18 17 16 15
        int minR = RC[2];
        int maxR = RC[3];
        int minC = RC[0];
        int maxC = RC[1];

        System.out.println(minR + " " + maxR + " " + minC + " " + maxC);
        for (int x = minR; x <= maxR; ++x) {
            for (int y = minC; y <= maxC; ++y) {
                xyz[0] = x;
                xyz[1] = y;
                System.out.println(x + " " + y);
                System.out.println(xyz2QuadTreeCode_1(xyz));
                //Result result = HbaseUtils.getResult("hbase_tile_table", layerName + xyz2QuadTreeCode(xyz));
                //byte[] str = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes("temp"));
                //System.out.println(layerName + xyz2QuadTreeCode(xyz));
                //if(str != null)
                //    System.out.println(str.length);
            }
        }
    }

    public static void xyz2QuadTreeCodes_TEST(int z) throws Exception {
        long[] xyz = new long[3];
        xyz[2] = z;
        //x是下一层的列号的倒叙  y是行号    如
        //下一层为  xmax = 20  xmin = 10
        //这层为    xmax = 10  xmin 5
        //x顺序为    20 19 18 17 16 15
        /*int minR = RC[2];
        int maxR = RC[3];
        int minC = RC[0];
        int maxC = RC[1];*/
        int minR =0; //3       22  12
        int maxR = 3;//6       23  12
        int minC = 0;//22       22  10
        int maxC = 2;//28      22  11  23  11   23 10  22  9   23  9

        System.out.println(minR + " " + maxR + " " + minC + " " + maxC);
        for (int x = minR; x <= maxR; ++x) {
            for (int y = minC; y <= maxC; ++y) {
                xyz[0] = x;
                xyz[1] = y;
                System.out.println(x + " " + y);
                System.out.println(xyz2QuadTreeCode_1(xyz));
                //Result result = HbaseUtils.getResult("hbase_tile_table", layerName + xyz2QuadTreeCode(xyz));
                //byte[] str = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes("temp"));
                //System.out.println(layerName + xyz2QuadTreeCode(xyz));
                //if(str != null)
                //    System.out.println(str.length);
            }
        }
    }
}
