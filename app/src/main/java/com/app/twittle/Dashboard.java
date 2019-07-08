package com.app.twittle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.twittle.adapter.CategoryAdapter;
import com.app.twittle.model.Category;
import com.app.twittle.retrofit.api.ApiServices;
import com.app.twittle.utils.CircularTextView;
import com.app.twittle.utils.ConnectionDetector;
import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Context mContext;
    ImageView btn_menu;
    NavigationView navigationView;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    Category getCategory;
    RecyclerView rv_cat;
    ImageView iv_cart;
    CircularTextView tv_cartcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_inc);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initView();

        if (cd.isConnected()) {
            parsejson();
        } else {
            Utility.showToastShort(mContext, getString(R.string.no_internet_msg));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));
        if (!Preferences.isgenerateUniqueKey(mContext)) {
            generateUniqueId();
            Preferences.setUniqueKey(mContext, true);
        }
    }

    private void generateUniqueId() {
        Random rand = new Random();
        String uniqueId = System.currentTimeMillis() + "" + rand.nextInt(500);
        Log.d("uniqueId", uniqueId);
        Preferences.set_UniqueId(mContext, uniqueId);
    }

    private void initView() {
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        rv_cat = findViewById(R.id.rv_cat);
        rv_cat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView txt_name = (TextView) headerView.findViewById(R.id.tv_user_name);
        ImageView ivNavDrawer = (ImageView) findViewById(R.id.iv_product_photo);
        txt_name.setText(Preferences.get_userName(mContext));

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(this);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Utility.openNavDrawer(id, mContext);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == btn_menu) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        }
    }

    private void parsejson() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Category> call = redditAPI.Getpet_category(Preferences.get_userId(mContext));
        call.enqueue(new Callback<Category>() {

            @Override
            public void onResponse(Call<Category> call, retrofit2.Response<Category> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    getCategory = response.body();
                    if (getCategory.getCategoryData().size() > 0) {
                        setuplistview();
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                pDialog.dismiss();
            }
        });

    }

    private void setuplistview() {
        CategoryAdapter ca = new CategoryAdapter(mContext, getCategory.getCategoryData());
        rv_cat.setAdapter(ca);
    }


}
