package com.sunji.tax;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sunji.tax.cost.Tax;
import com.sunji.tax.cost.TaxInterface;
import com.sunji.tax.cost.bean.TaxCost;

/**
 * des:主页面
 * verison:1.0
 * author:sunji
 * create time:2019/1/3 17:02
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private EditText ed_social;
    private EditText edit_reserve_funds;
    private EditText edit_children;
    private EditText edit_education;
    private EditText edit_rant;
    private EditText edit_parent;
    private EditText edit_medical;
    private FloatingActionButton fab;
    private NavigationView nav_view;
    private DrawerLayout drawer_layout;
    private EditText ed_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        ed_income.setText("");
        ed_social.setText("");
        edit_reserve_funds.setText("");
        edit_children.setText("");
        edit_education.setText("");
        edit_rant.setText("");
        edit_parent.setText("");
        edit_medical.setText("");
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "分享"));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_share) {
            share();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ed_social = findViewById(R.id.ed_social);
        edit_reserve_funds = findViewById(R.id.edit_reserve_funds);
        edit_children = findViewById(R.id.edit_children);
        edit_education = findViewById(R.id.edit_education);
        edit_rant = findViewById(R.id.edit_rant);
        edit_parent = findViewById(R.id.edit_parent);
        edit_medical = findViewById(R.id.edit_medical);
        fab = findViewById(R.id.fab);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        drawer_layout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        fab.setOnClickListener(this);
        ed_income = findViewById(R.id.ed_income);
        ed_income.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        String income = ed_income.getText().toString().trim();
        if (TextUtils.isEmpty(income)) {
            Toast.makeText(this, "请输入薪酬(月)", Toast.LENGTH_SHORT).show();
            return;
        }
        String social = ed_social.getText().toString().trim();
        if (TextUtils.isEmpty(social)) {
            Toast.makeText(this, "请输入社保扣除额(月)", Toast.LENGTH_SHORT).show();
            return;
        }
        String funds = edit_reserve_funds.getText().toString().trim();
        String children = edit_children.getText().toString().trim();
        String education = edit_education.getText().toString().trim();
        String rant = edit_rant.getText().toString().trim();
        String parent = edit_parent.getText().toString().trim();
        String medical = edit_medical.getText().toString().trim();
        TaxInterface taxInterface = new Tax();
        TaxCost taxCost = taxInterface.getTaxInfo(income, social, funds, children, education, rant, parent, medical);
        startActivity(new Intent(this, TaxInfoActivity.class).putExtra("data", taxCost));
    }

}
