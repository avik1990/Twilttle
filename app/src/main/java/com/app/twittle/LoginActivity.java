package com.app.twittle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.twittle.model.ProductModel;
import com.app.twittle.model.Registration;
import com.app.twittle.retrofit.api.ApiServices;
import com.app.twittle.utils.ConnectionDetector;
import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    EditText et_name;
    EditText et_email;
    EditText et_phoneno;
    Button btn_register;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    String user_name, user_email, user_phone;
    Registration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        cd = new ConnectionDetector(mContext);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please Wait...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);

        initViews();

    }

    private void initViews() {
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_phoneno = findViewById(R.id.et_phoneno);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btn_register) {
            if (et_name.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Your Name");
                return;
            }

            if (et_email.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Your Email");
                return;
            }

            if (!Utility.isValidEmail(et_email.getText().toString())) {
                Utility.showToastShort(mContext, "Please Enter Valid Email");
                return;
            }

            if (et_phoneno.getText().toString().isEmpty()) {
                Utility.showToastShort(mContext, "Please Enter Your Phone No.");
                return;
            }

            user_name = et_name.getText().toString().trim();
            user_email = et_email.getText().toString().trim();
            user_phone = et_phoneno.getText().toString().trim();


            verifyUser();

        }
    }

    private void verifyUser() {
        pDialog.show();
        String BASE_URL = getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<Registration> call = redditAPI.UserRegistration(user_name, user_email, user_phone);
        call.enqueue(new Callback<Registration>() {

            @Override
            public void onResponse(Call<Registration> call, retrofit2.Response<Registration> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    registration = response.body();
                    if (registration.getAck().equals("1")) {
                        if (registration.getUserData().size() > 0) {
                            Preferences.setisVerified(mContext, true);
                            Preferences.set_userName(mContext, registration.getUserData().get(0).getName());
                            Preferences.set_userEmail(mContext, registration.getUserData().get(0).getEmail());
                            Preferences.set_userPhone(mContext, registration.getUserData().get(0).getPhone());
                            Preferences.set_userId(mContext, registration.getUserData().get(0).getUserId());
                            Intent intent = new Intent(mContext, Dashboard.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    } else {
                        Utility.showToastShort(mContext, registration.getMsg());
                    }
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Registration> call, Throwable t) {
                pDialog.dismiss();
            }
        });
    }
}
