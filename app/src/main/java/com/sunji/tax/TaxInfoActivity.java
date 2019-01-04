package com.sunji.tax;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunji.tax.cost.bean.TaxCost;

/**
 * des:详细个税情况
 * verison:1.0
 * author:sunji
 * create time:2019/1/4 10:21
 */
public class TaxInfoActivity extends AppCompatActivity {
    private TextView tv_info;
    private LinearLayout info_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_info);
        initView();
        setData();
    }


    private void initView() {
        tv_info = findViewById(R.id.tv_info);
        info_layout = findViewById(R.id.info_layout);
    }

    private void setData() {
        TaxCost taxCost = (TaxCost) getIntent().getSerializableExtra("data");
        if (taxCost != null) {
            tv_info.setText(taxCost.toString());
        }
    }
}
