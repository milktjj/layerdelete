package com.iecas.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;

import java.util.ArrayList;
import java.util.List;

public class QuadTreeUtil {
    private static final Log log = LogFactory.getLog(QuadTreeUtil.class);

    public QuadTreeUtil() {
    }

    public static String xyz2QuadTreeCode(long[] xyz) {
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

        for(int i = 0; (long)i <= numOfTier; ++i) {
            long mid = (long)Math.pow(2.0D, (double)(numOfTier - (long)i)) - 1L;
            if (x > mid && y > mid) {
                sb.append(2);
            } else if (x > mid && y <= mid) {
                sb.append(3);
            } else if (x <= mid && y > mid) {
                sb.append(1);
            } else {
                sb.append(0);
            }

            x = x % (long)Math.pow(2.0D, (double)(numOfTier - (long)i));
            y = y % (long)Math.pow(2.0D, (double)(numOfTier - (long)i));
        }

        return sb.toString();
    }

    public static void xyz2QuadTreeCodes(int[] RC, int z, String layerName) throws Exception {
        long[] xyz = new long[3];
        xyz[2] = z;
        int minR = RC[0];
        int maxR = RC[1];
        int minC = RC[2];
        int maxC = RC[3];
        System.out.println(minR + " " + maxR + " " + minC + " " + maxC);
        for (int x = minR; x < maxR; ++x) {
            for (int y = minC; y < maxC; ++y) {
                xyz[0] = x;
                xyz[1] = y;
                Result result = HbaseUtils.getResult("hbase_tile_table", layerName + '_' +xyz2QuadTreeCode(xyz));
                log.info(layerName + '_' +xyz2QuadTreeCode(xyz));
                System.out.println(result.size());
            }
        }
    }
}
