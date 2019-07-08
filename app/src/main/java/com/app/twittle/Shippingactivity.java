package com.app.twittle;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.twittle.model.CartDeleteAction;
import com.app.twittle.model.Registration;
import com.app.twittle.retrofit.api.ApiServices;
import com.app.twittle.utils.ConnectionDetector;
import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class Shippingactivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    EditText et_name;
    EditText et_email;
    EditText et_phoneno;
    Button btn_placeorder;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_name, user_email, user_phone, user_address, user_city, user_state, user_pincode;
    CartDeleteAction registration;
    TextView tv_pagename;
    EditText et_address;
    EditText et_city;
    EditText et_state;
    EditText et_pincode;
    FrameLayout cartvie;
    ImageView btn_menu, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    private void initViews() {
        cartvie = findViewById(R.id.cartvie);
        cartvie.setVisibility(View.GONE);
        tv_pagename = findViewById(R.id.tv_pagename);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_phoneno = findViewById(R.id.et_phoneno);
        btn_placeorder = findViewById(R.id.btn_placeorder);
        tv_pagename.setText("Shipping Details");
        btn_placeorder.setOnClickListener(this);

        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setVisibility(View.GONE);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setVisibility(View.VISIBLE);

        et_address = findViewById(R.id.et_address);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_pincode = findViewById(R.id.et_pincode);
        btn_menu.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        et_name.setText(Preferences.get_userName(mContext));
        et_email.setText(Preferences.get_userEmail(mContext));
        et_phoneno.setText(Preferences.get_userPhone(mContext));
        et_pincode.setEnabled(false);
        et_pincode.setText(Preferences.get_Zip(mContext));
    }


    @Override
    public void onClick(View v) {
        if (v == btn_placeorder) {
            if (et_name.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Name");
                return;
            }

            if (et_email.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Email");
                return;
            }

            if (!Utility.isValidEmail(et_email.getText().toString())) {
                Utility.showToastShort(mContext, "Please Enter Valid Email");
                return;
            }

            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Phone No.");
                return;
            }


            if (et_address.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Address");
                return;
            }

            if (et_city.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter City");
                return;
            }

            if (et_state.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter State");
                return;
            }

            if (et_pincode.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Pin Code");
                return;
            }


            user_address = et_address.getText().toString().trim();
            user_city = et_city.getText().toString().trim();
            user_state = et_state.getText().toString().trim();
            user_pincode = et_pincode.getText().toString().trim();

            user_name = et_name.getText().toString().trim();
            user_email = et_email.getText().toString().trim();
            user_phone = et_phoneno.getText().toString().trim();


            postShippingDetails();

        } else if (v == btn_back) {
            onBackPressed();
            finish();
        }
    }

    private void postShippingDetails() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);


        Call<CartDeleteAction> call = redditAPI.PostShipping(user_name, user_email, user_phone, user_address, user_city, user_state, user_pincode, Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck().equals("1")) {
                        Preferences.set_Cartount(mContext, "0");
                        getMessage();
                    } else {
                        Utility.showToastShort(mContext, registration.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    private void getMessage() {
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);


        Call<CartDeleteAction> call = redditAPI.GetCartThankyouMessage(Preferences.get_userId(mContext), Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck().equals("1")) {
                        showDialog(mContext, registration.getMsg());
                    } else {
                        Utility.showToastShort(mContext, registration.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }


    public void showDialog(Context activity, String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_thankyou_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView et_pincode = dialog.findViewById(R.id.et_pincode);
        et_pincode.setText(message);
        FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.setUniqueKey(mContext, false);
                Intent i = new Intent(mContext, Dashboard.class);
                startActivity(i);
                finishAffinity();
            }
        });

        dialog.show();
    }

}
