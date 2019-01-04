package com.sunji.tax.cost.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * des:金额计算工具方法
 * verison:1.0
 * author:sunji
 * create time:2018/12/3 18:00
 */
public class MoneyUtil {
    private static final int DEF_DIV_SCALE = 2;
    private static final int DEF_DIV_SCALE5 = 5;


    private MoneyUtil() {
    }

    public static boolean isInvalid(BigDecimal value) {
        return value == null || value.doubleValue() == 0.0D;
    }

    public static boolean isNull(BigDecimal value) {
        return value == null;
    }

    public static String getFormate(BigDecimal value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    /**
     * 返回两个数求和后的字符串
     *
     * @param value1
     * @param value2
     * @return 如果参数存在空或者0，则返回null
     */
    public static String add2String(BigDecimal value1, BigDecimal value2) {
        return !isNull(value1) && !isNull(value2) ? getFormate(value1.add(value2)) : null;
    }

    /**
     * 两个数求和
     *
     * @param decimal1
     * @param decimal2
     * @return 如果参数有一个为null或者0，则返回空
     */
    public static BigDecimal add(BigDecimal decimal1, BigDecimal decimal2) {
        return !isNull(decimal1) && !isNull(decimal2) ? decimal1.add(decimal2) : null;
    }

    /**
     * 两个数的差值
     *
     * @param value1
     * @param value2
     * @return 如果参数有一个为空或者等于0，则返回0
     */
    public static String substract2String(BigDecimal value1, BigDecimal value2) {
        return !isNull(value1) && !isNull(value2) ? getFormate(value1.subtract(value2)) : "0.00";
    }

    /**
     * 两个数的差值
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal substract(BigDecimal value1, BigDecimal value2) {
        return !isNull(value1) && !isNull(value2) ? value1.subtract(value2) : null;
    }

    /**
     * 比较两个数。
     *
     * @param before
     * @param after
     * @return 如果参数存在为空或者0，则返回500.否则则返回0，-1，
     * 如果 before > after 返回1, 如果 before < after，返回-1,  如果before 等于 after则返回0
     */
    public static int compareTo(BigDecimal before, BigDecimal after) {
        return !isNull(before) && !isNull(after) ? before.compareTo(after) : 500;
    }

    /**
     * 两个数的乘积字符串
     *
     * @param before
     * @param after
     * @return 如果参数存在null或者0，则返回0
     */
    public static String multiply2String(BigDecimal before, BigDecimal after) {
        return !isNull(before) && !isNull(after) ? getFormate(before.multiply(after)) : "0.00";
    }

    /**
     * 求两个数的乘积
     *
     * @param value1
     * @param value2
     * @return 参数如果存在空或者0，则返回null
     */
    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        return !isNull(value1) && !isNull(value2) ? value1.multiply(value2) : null;
    }

    /**
     * 求两个数的商
     *
     * @param before
     * @param after
     * @return 参数如果存在null或者0，则返回null
     */
    public static String divide2String(BigDecimal before, BigDecimal after) {
        return !isNull(before) && !isInvalid(after) ?
                getFormate(before.divide(after, DEF_DIV_SCALE, 4)) : null;
    }

    public static BigDecimal divide(BigDecimal value1, BigDecimal value2) {
        return !isNull(value1) && !isInvalid(value2) ? value1.divide(value2, DEF_DIV_SCALE5, 4) : null;
    }
}
