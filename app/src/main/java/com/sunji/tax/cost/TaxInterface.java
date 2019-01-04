package com.sunji.tax.cost;


import com.sunji.tax.cost.bean.TaxCost;

import java.security.InvalidParameterException;

/**
 * des:2019新个税
 * verison:1.0
 * author:sunji
 * create time:2019/1/3 17:02
 */
public interface TaxInterface {
    /**
     * 计算交税后金额(不包括大病医疗)
     *
     * @param salary      薪酬(月薪)

     * @param attachMoney 扣除数 ：三险一金，然后再按顺序为子女教育，继续教育，住房贷款，租房，赡养，大病
     * @return
     */
    public TaxCost getTaxInfo(
            String salary, String... attachMoney
    ) throws InvalidParameterException;

    /**
     * 计算交税后金额(不包括大病医疗)
     *
     * @param salary      年薪薪酬

     * @param attachMoney 专项扣除数，按顺序为子女教育，继续教育，住房贷款，租房，赡养，大病
     * @return
     */
    public TaxCost getTaxInfoByYear(String salary, String... attachMoney) throws InvalidParameterException;
}
