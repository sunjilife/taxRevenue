package com.sunji.tax.cost;


import android.text.TextUtils;

import com.sunji.tax.cost.bean.TaxCost;
import com.sunji.tax.cost.util.MoneyUtil;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.Arrays;


/**
 * des:计算个税费用减除
 * verison:1.0
 * author:sunji
 * create time:2019/1/3 17:02
 */
public class Tax implements TaxInterface {
    /**
     * 达到扣个税的门槛金额-月份
     */
    public static final float MONTH_THRESHOLD_MONEY = 5000;
    /**
     * 达到扣个税的金额门槛-年度
     */
    public static final float YEAR_THRESHOLD_MONEY = 60000;

    /**
     * 对应的累计金额税率
     */
    public final float RATE36000 = 3;
    public final float RATE144000 = 10;
    public final float RATE300000 = 20;
    public final float RATE420000 = 25;
    public final float RATE660000 = 30;
    public final float RATE960000 = 35;
    public final float RATE_MAX = 45;

    /**
     * 累计额
     */
    public final float BASE_THRESHOLD_36000 = 36000;
    public final float BASE_THRESHOLD_144000 = 144000;
    public final float BASE_THRESHOLD_300000 = 300000;
    public final float BASE_THRESHOLD_420000 = 420000;
    public final float BASE_THRESHOLD_660000 = 660000;
    public final float BASE_THRESHOLD_960000 = 960000;

    /**
     * 扣除数
     */
    public final float DEDUCT_VALUE_36000 = 0;
    public final float DEDUCT_VALUE_144000 = 2520;
    public final float DEDUCT_VALUE_300000 = 16920;
    public final float DEDUCT_VALUE_420000 = 31920;
    public final float DEDUCT_VALUE_660000 = 52920;
    public final float DEDUCT_VALUE_960000 = 85920;
    public final float DEDUCT_VALUE_MAX = 181920;


    private boolean isNull(String s) {
        if (s == null || s.equals(""))
            return true;
        return false;
    }

    /**
     * 获取预扣率和速算扣除数
     *
     * @param money
     * @return
     */
    private float[] getRateAndDeduct(float money) {
        float[] result = new float[2];
        if (money <= BASE_THRESHOLD_36000) {
            result[0] = RATE36000;
            result[1] = DEDUCT_VALUE_36000;
            return result;
        }
        if (money <= BASE_THRESHOLD_144000) {
            result[0] = RATE144000;
            result[1] = DEDUCT_VALUE_144000;
            return result;
        }
        if (money <= BASE_THRESHOLD_300000) {
            result[0] = RATE300000;
            result[1] = DEDUCT_VALUE_300000;
            return result;
        }
        if (money <= BASE_THRESHOLD_420000) {
            result[0] = RATE420000;
            result[1] = DEDUCT_VALUE_420000;
            return result;
        }
        if (money <= BASE_THRESHOLD_660000) {
            result[0] = RATE660000;
            result[1] = DEDUCT_VALUE_660000;
            return result;
        }
        if (money <= BASE_THRESHOLD_960000) {
            result[0] = RATE960000;
            result[1] = DEDUCT_VALUE_960000;
            return result;
        }
        result[0] = RATE_MAX;
        result[1] = DEDUCT_VALUE_MAX;
        return result;
    }


    /**
     * 以下方法计算各月应预扣预缴税额：(30000×2-5000×2-4500×2-2000×2) ×10%-2520 -555 =625 元；
     *
     * @param monthIndex  月份索引 1-12 对应自然月份1-12月
     * @param salary      月收入
     * @param lastValue   上个月预扣税额
     * @param attachMoney 附加税额
     * @return
     */
    private BigDecimal countMoney(int monthIndex, BigDecimal salary, BigDecimal[] lastValue, String... attachMoney) {
        //累计预扣预缴应纳税所得额 =月薪酬*月份
        salary = MoneyUtil.multiply(salary, new BigDecimal(monthIndex));
        BigDecimal totalAttach = new BigDecimal(0);
        //累加各项专项扣除
        if (attachMoney != null && attachMoney.length > 0) {
            for (int i = 0; i < attachMoney.length; i++) {
                String value = attachMoney[i];
                if (!TextUtils.isEmpty(value)) {
                    totalAttach = MoneyUtil.add(totalAttach, new BigDecimal(value));
                }
            }
            //添加5000基础减除费用
            totalAttach = MoneyUtil.add(totalAttach, new BigDecimal(MONTH_THRESHOLD_MONEY));
        }
        //月份*单月扣除额=总扣除额
        BigDecimal temp = MoneyUtil.multiply(totalAttach, new BigDecimal(monthIndex));
        temp = MoneyUtil.substract(salary, temp);
        float[] rate = getRateAndDeduct(temp.floatValue());
        ////乘以预扣率 3%
        BigDecimal middleDecimal = MoneyUtil.multiply(temp, new BigDecimal(rate[0]));
        middleDecimal = MoneyUtil.divide(middleDecimal, new BigDecimal(100));
        //先减去当月速算扣除数
        middleDecimal = MoneyUtil.substract(middleDecimal, new BigDecimal(rate[1]));
        //再减去上月扣除额度
        if (lastValue != null && lastValue.length > 0) {
            for (int i = 0; i < monthIndex; i++) {
                BigDecimal b = lastValue[i];
                if (b != null) {
                    middleDecimal = MoneyUtil.substract(middleDecimal, b);
                }
            }
        }
        return middleDecimal;
    }


    @Override
    public TaxCost getTaxInfo(String salary, String... attachMoney) throws InvalidParameterException {
        if (isNull(salary)) {
            throw new InvalidParameterException("请输入您的月薪");
        }
        BigDecimal m12 = new BigDecimal(12);
        BigDecimal totalMoney = new BigDecimal(salary);
        BigDecimal[] allMonth = new BigDecimal[12];
        if (totalMoney.floatValue() > MONTH_THRESHOLD_MONEY) {
            //暂存每个月扣除额
            BigDecimal totalTax = new BigDecimal(0);
            for (int i = 0; i < 12; i++) {
                allMonth[i] = countMoney(i + 1, totalMoney, allMonth, attachMoney);
                System.out.println("预扣" + (i + 1) + "月份:" + allMonth[i].toString());
                totalTax = MoneyUtil.add(totalTax, allMonth[i]);
            }
            totalMoney = MoneyUtil.multiply(totalMoney, m12);
            return new TaxCost(totalMoney, totalTax).setAllMonth(Arrays.asList(allMonth));
        } else {
            totalMoney = MoneyUtil.multiply(totalMoney, m12);
            return new TaxCost(totalMoney, new BigDecimal(0));
        }
    }

    @Override
    public TaxCost getTaxInfoByYear(String salary, String... attachMoney) throws
            InvalidParameterException {
        if (isNull(salary)) {
            throw new InvalidParameterException("请输入您的年薪");
        }
        BigDecimal total = new BigDecimal(salary);
        if (total.floatValue() > YEAR_THRESHOLD_MONEY) {
            BigDecimal allMonth = new BigDecimal(12);
            return getTaxInfo(MoneyUtil.divide(total, allMonth).toString(), attachMoney);
        } else {
            return new TaxCost(total, new BigDecimal(0));
        }
    }
}
