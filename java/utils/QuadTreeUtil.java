package utils;


public class QuadTreeUtil {
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
}
