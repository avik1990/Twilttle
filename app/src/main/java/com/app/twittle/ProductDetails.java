package com.app.twittle;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.twittle.adapter.ProdductAdapter;
import com.app.twittle.model.AddToCart;
import com.app.twittle.model.MyCart;
import com.app.twittle.model.ProductModel;
import com.app.twittle.model.ZipCodeVerify;
import com.app.twittle.retrofit.api.ApiServices;
import com.app.twittle.utils.CircularTextView;
import com.app.twittle.utils.ConnectionDetector;
import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class ProductDetails extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView btn_menu, btn_back, iv_productimg;
    ConnectionDetector cd;
    String category_name;
    TextView tv_pagename;
    TextView tv_title, tv_desc, tv_price;
    Spinner spinner;
    int position = 0;
    String[] items;
    RelativeLayout rl_quantity;
    TextView tv_quantity;
    Button btn_addtocart;
    ProgressDialog pDialog;
    String product_id = "", packet_id = "";
    RelativeLayout v_spinner;
    AddToCart addToCart;
    MyCart myCart;
    CircularTextView tv_cartcount;
    ImageView iv_cart;
    public static ProductModel productModel;
    LinearLayout footer;
    Button btn_backhome;

    android.support.v7.widget.SearchView v_searcview;
    ImageView iv_search;
    EditText searchEditText;
    String category_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        cd = new ConnectionDetector(mContext);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        position = Integer.parseInt(getIntent().getStringExtra("position").trim());
        category_id = getIntent().getExtras().getString("cat_id");

        Log.d("JsonData", ProductPage.productModel.getProductData().get(position).getProductDetails());

        initView();
    }


    private void initView() {
        btn_backhome = findViewById(R.id.btn_backhome);
        footer = findViewById(R.id.footer);
        tv_pagename = findViewById(R.id.tv_pagename);
        btn_back = findViewById(R.id.btn_back);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        v_spinner = findViewById(R.id.v_spinner);
        v_spinner.setVisibility(View.GONE);
        iv_productimg = findViewById(R.id.iv_productimg);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        tv_cartcount = (CircularTextView) findViewById(R.id.tv_cartcount);
        String colorStr = getResources().getString(R.string.green_color);
        tv_cartcount.setSolidColor(colorStr);
        spinner = findViewById(R.id.spinner);
        iv_cart = findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(this);
        rl_quantity = findViewById(R.id.rl_quantity);
        tv_quantity = findViewById(R.id.tv_quantity);
        tv_price = findViewById(R.id.tv_price);
        btn_addtocart = findViewById(R.id.btn_addtocart);
        v_searcview = findViewById(R.id.v_searcview);
        v_searcview.setOnClickListener(this);
        iv_search = findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
        iv_search.setOnClickListener(this);
        btn_addtocart.setOnClickListener(this);
        btn_backhome.setOnClickListener(this);
        setData();

        /*v_searcview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Utility.showToastShort(mContext, "Hello");
                return false;
            }
        });*/
        searchEditText = v_searcview.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setCursorVisible(true);
        /*searchEditText.setHintTextColor(getResources().getColor(R.color.black));*/

        ImageView closeButton = (ImageView) this.v_searcview.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iv_search.setVisibility(View.VISIBLE);
                v_searcview.setVisibility(View.GONE);
                tv_pagename.setVisibility(View.VISIBLE);
                searchEditText.setText("");
            }
        });


        v_searcview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Utility.showToastShort(mContext, s.toString());
                getSearchedProduct(s.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //  tv_pagename.setText(ProductPage.productModel.getProductData().get(position).getProductDetails());
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_cartcount.setText(Preferences.get_Cartount(mContext));

    }

    private void setData() {
        tv_title.setText(ProductPage.productModel.getProductData().get(position).getProductName());
        tv_desc.setText(ProductPage.productModel.getProductData().get(position).getProductDetails());
        product_id = ProductPage.productModel.getProductData().get(position).getProductId();

        try {
            Picasso.with(mContext)
                    .load(ProductPage.productModel.getProductData().get(position).getProductPhoto())
                    .into(iv_productimg, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {
                            // progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            //progressView.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {

        }

        if (ProductPage.productModel.getProductData().get(position).getPackets().size() > 0) {
            v_spinner.setVisibility(View.VISIBLE);
            items = new String[ProductPage.productModel.getProductData().get(position).getPackets().size() + 1];
            items[0] = "Select Packet";
            for (int i = 0; i < ProductPage.productModel.getProductData().get(position).getPackets().size(); i++) {
                items[i + 1] = ProductPage.productModel.getProductData().get(position).getPackets().get(i).packetSize + " - " + ProductPage.productModel.getProductData().get(position).getPackets().get(i).getPrice();
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                    if (myPosition > 0) {
                        footer.setVisibility(View.VISIBLE);
                        v_spinner.setVisibility(View.VISIBLE);
                        rl_quantity.setVisibility(View.VISIBLE);
                        tv_quantity.setText(ProductPage.productModel.getProductData().get(position).getPackets().get(myPosition - 1).packetSize);
                        tv_price.setText(ProductPage.productModel.getProductData().get(position).getPackets().get(myPosition - 1).getPrice());
                        packet_id = ProductPage.productModel.getProductData().get(position).getPackets().get(myPosition - 1).getPacketId();
                    } else {
                        footer.setVisibility(View.GONE);
                        rl_quantity.setVisibility(View.GONE);
                    }

                   /* Log.i("renderSpinner -> ", "onItemSelected: " + myPosition + "/" + myID);

                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.GREEN);
                    ((TextView) parentView.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (varScaleY * 22.0f) );
                    ((TextView) parentView.getChildAt(0)).setPadding(1,1,1,1);*/


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

        } else {
            footer.setVisibility(View.GONE);
            v_spinner.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == btn_back) {
            finish();
            onBackPressed();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else if (v == btn_addtocart) {
            AddToCart();
        } else if (v == iv_cart) {
            Intent i = new Intent(mContext, ProductCart.class);
            i.putExtra("From", "Dashboard");
            startActivity(i);
        } else if (v == iv_search) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(v_searcview.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
            v_searcview.requestFocus();
            iv_search.setVisibility(View.GONE);
            v_searcview.setVisibility(View.VISIBLE);
            tv_pagename.setVisibility(View.GONE);
            searchEditText.setText("");
        } else if (v == btn_backhome) {
            finish();
        }
    }

    private void AddToCart() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<AddToCart> call = redditAPI.AddToCart(Preferences.get_userId(mContext), product_id, packet_id, Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<AddToCart>() {

            @Override
            public void onResponse(Call<AddToCart> call, retrofit2.Response<AddToCart> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    addToCart = response.body();
                    if (addToCart.getAck().equals("1")) {
                        LoadCartProduct();
                        Utility.showToastShort(mContext, addToCart.getMsg());
                      /*  Intent i = new Intent(mContext, ProductCart.class);
                        startActivity(i);*/
                    } else {
                        Utility.showToastShort(mContext, addToCart.getMsg());
                    }
                }
                // pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<AddToCart> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    public void LoadCartProduct() {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<MyCart> call = redditAPI.GetMyCart(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<MyCart>() {

            @Override
            public void onResponse(Call<MyCart> call, retrofit2.Response<MyCart> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    myCart = response.body();
                    if (myCart.getAck().equals("1")) {
                        Preferences.set_Cartount(mContext, myCart.getPriceData().getTotal_quantity());
                        tv_cartcount.setText(Preferences.get_Cartount(mContext));
                    } else {
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MyCart> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }

    private void getSearchedProduct(final String searchString) {
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
                            Intent i = new Intent(mContext, SearchedProductPage.class);
                            i.putExtra("searchQuery", searchString);
                            i.putExtra("cat_id", category_id);
                            startActivity(i);
                            //inflateAdapter();
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


}
