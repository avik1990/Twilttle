package com.app.twittle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.twittle.adapter.ProdductAdapter;
import com.app.twittle.model.ProductModel;
import com.app.twittle.retrofit.api.ApiServices;
import com.app.twittle.utils.CircularTextView;
import com.app.twittle.utils.ConnectionDetector;
import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchedProductPage extends AppCompatActivity implements View.OnClickListener, ProdductAdapter.AdapterPos {

    Context mContext;
    ImageView btn_menu, btn_back;
    RecyclerView rv_product_listing;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    public static ProductModel productModel;
    String category_id;
    TextView tv_pagename;
    CircularTextView tv_cartcount;
    ImageView iv_cart;
    String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        category_id = getIntent().getExtras().getString("cat_id");
        searchQuery = getIntent().getExtras().getString("searchQuery");
        initView();

        if (cd.isConnected()) {
            parsejson(searchQuery);
        } else {
            Utility.showToastShort(mContext, getString(R.string.no_internet_msg));
        }

    }


    private void parsejson(final String searchString) {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        //Getsearch_food_list(@Query("category_id") String category_id, @Query("user_id") String user_id, @Query("unique_id") String unique_id, @Query("search_string") String search_string);

        Call<ProductModel> call = redditAPI.Getsearch_food_list(category_id, Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext), searchString);
        call.enqueue(new Callback<ProductModel>() {

            @Override
            public void onResponse(Call<ProductModel> call, retrofit2.Response<ProductModel> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    productModel = response.body();
                    if (productModel.getAck() == 1) {
                        if (productModel.getProductData().size() > 0) {
                            inflateAdapter();
                        }
                    } else {
                        Utility.showToastShort(mContext, productModel.msg);
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void initView() {
        rv_product_listing = findViewById(R.id.rv_product_listing);
        rv_product_listing.setLayoutManager(new GridLayoutManager(mContext, 2));
        tv_pagename = findViewById(R.id.tv_pagename);
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        tv_pagename.setText("Search Result");
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));

    }

    private void inflateAdapter() {
        ProdductAdapter ca = new ProdductAdapter(mContext, productModel.getProductData(), this);
        rv_product_listing.setAdapter(ca);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        }
    }


    @Override
    public void adapterPosition(int pos) {
        //Utility.showToastShort(mContext, "" + productModel.getProductData());
        Intent a = new Intent(mContext, ProductDetails.class);
        a.putExtra("position", "" + pos);
        a.putExtra("cat_id", "" + category_id);

        mContext.startActivity(a);
    }
}
