package com.sunji.tax.cost.bean;

import com.sunji.tax.cost.Tax;
import com.sunji.tax.cost.util.MoneyUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * des:封装费用扣除
 * verison:1.0
 * author:sunji
 * create time:2019/1/3 17:02
 */
public class TaxCost implements Serializable {
    public BigDecimal totalSalary;
    public BigDecimal totalTaxRevenue;
    public List<BigDecimal> allMonth;

    public TaxCost(BigDecimal totalSalary, BigDecimal totalTaxRevenue) {
        this.totalSalary = totalSalary;
        this.totalTaxRevenue = totalTaxRevenue;
    }

    public List<BigDecimal> getAllMonth() {
        return allMonth;
    }

    public TaxCost setAllMonth(List<BigDecimal> allMonth) {
        this.allMonth = allMonth;
        return this;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public BigDecimal getTotalTaxRevenue() {
        return totalTaxRevenue;
    }

    public void setTotalTaxRevenue(BigDecimal totalTaxRevenue) {
        this.totalTaxRevenue = totalTaxRevenue;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("");
        if (totalSalary != null) {
            builder.append("您的总收入:" + totalSalary.toString() + "\n\n");
            if (totalSalary.floatValue() < Tax.YEAR_THRESHOLD_MONEY) {
                builder.append("\n未达到扣税标准，您不需要交税\n");
                return builder.toString();
            }
        }

        if (allMonth != null && allMonth.size() > 0) {
            for (int i = 0; i < allMonth.size(); i++) {
                BigDecimal bigDecimal = allMonth.get(i);
                if (!MoneyUtil.isNull(bigDecimal)) {
                    String s = String.format("第%d个月预扣金额为:%f\n", i + 1, bigDecimal.floatValue());
                    builder.append(s);
                }
            }
        }

        if (totalTaxRevenue != null) {
            builder.append("\n累计总扣税:" + totalTaxRevenue.toString() + "\n");
        }
        return builder.toString();
    }
}
