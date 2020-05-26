package space.code.fei.util;

import java.io.File;
import java.util.UUID;

/**
 * UUID工具
 */
public class UUIDUtils {

    /**
     * 获得一个去掉“-”符号的UUID
     *
     * @return StringUUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获得一个第一位是“-”符号剩余是数字的UUID(加“-”符号一共20位)
     *
     * @return StringUUID
     */
    public static String getNumUUID() {
        return String.valueOf(UUID.randomUUID().getLeastSignificantBits());
    }

    /**
     * 获得纯数字的UUID(19位)
     *
     * @return StringUUID
     */
    public static String getJustNumUUID() {
        String s = String.valueOf(UUID.randomUUID().getLeastSignificantBits());
        s = s.substring(1);
        return s;
    }

    /**
     * 获得指定数目标UUID
     *
     * @param number int 必需获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }

        return ss;
    }

}