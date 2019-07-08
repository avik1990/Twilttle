package com.app.twittle.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.twittle.ProductCart;
import com.app.twittle.R;
import com.app.twittle.model.CartDeleteAction;
import com.app.twittle.model.MyCart;
import com.app.twittle.model.ZipCodeVerify;
import com.app.twittle.retrofit.api.ApiServices;
import com.app.twittle.utils.ConnectionDetector;
import com.app.twittle.utils.Preferences;
import com.app.twittle.utils.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private List<MyCart.CartDatum> moviesList;
    private List<MyCart.CartDatum> moviesList1;
    private List<MyCart.CartDatum> mArrayList;
    Context mContext;
    private int amount = 0;
    MyCart.CartDatum movie;
    MyViewHolder holder1;
    ProgressDialog progressDialog;
    String cartstringjson = "";
    String cart_id = "";
    ConnectionDetector cd;
    CartDeleteAction cartDeleteAction;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_productname, tv_size, tv_producttype, tv_price, tv_position;
        ImageView iv_sub, iv_add;
        EditText et_qty;
        ImageView iv_product;
        ProgressBar progressbar;
        TextView tv_id, tv_unitprice, tv_quantity;
        Button btn_delete, btn_update;

        public MyViewHolder(View view) {
            super(view);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            tv_producttype = (TextView) view.findViewById(R.id.tv_producttype);
            tv_size = (TextView) view.findViewById(R.id.tv_size);
            tv_position = (TextView) view.findViewById(R.id.tv_position);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            iv_sub = (ImageView) view.findViewById(R.id.iv_sub);
            iv_add = (ImageView) view.findViewById(R.id.iv_add);
            et_qty = (EditText) view.findViewById(R.id.et_qty);
            iv_product = (ImageView) view.findViewById(R.id.iv_product);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
            btn_delete = view.findViewById(R.id.btn_delete);
            btn_update = view.findViewById(R.id.btn_update);
            tv_unitprice = view.findViewById(R.id.tv_unitprice);
            tv_quantity = view.findViewById(R.id.tv_quantity);

            // this.myCustomEditTextListener = myCustomEditTextListener;
            //et_qty.addTextChangedListener(myCustomEditTextListener);
        }
    }

    public CartAdapter(List<MyCart.CartDatum> moviesList, Context mContext) {
        this.moviesList = moviesList;
        this.moviesList1 = moviesList;
        this.mArrayList = moviesList;
        this.mContext = mContext;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        cd = new ConnectionDetector(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder1 = holder;
        movie = moviesList.get(position);
        holder.tv_productname.setText(movie.getProductName());
        // holder.tv_producttype.setText(movie.getModel());
        //holder.tv_position.setText(movie.getPosition());
        holder.tv_id.setText(movie.getCartId());
        holder.et_qty.setText(String.valueOf(movie.quantity));

        holder.tv_unitprice.setText("Unit Price : " + "\u20B9" + " " + String.valueOf(movie.unitPrice));
        holder.tv_quantity.setText("Packet      : " + String.valueOf(movie.quantity) + " Pc(s)");
        holder.tv_price.setText("Sub-Total  : " + "\u20B9" + " " + movie.getUnitPrice());

        try {
            Picasso.with(mContext)
                    .load(movie.getProductPhoto())
                    .into(holder.iv_product, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.iv_product.setImageResource(R.mipmap.ic_launcher);
                        }
                    });
        } catch (Exception e) {
        }


        try {
            if (!holder.et_qty.getText().equals("0") && !holder.et_qty.getText().toString().isEmpty()) {
                amount = Integer.parseInt(holder.et_qty.getText().toString().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("You want to delete this item.")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (cd.isConnected()) {
                                    dialog.dismiss();
                                    deletefromcart(movie.getCartId());
                                } else {
                                    Utility.showToastShort(mContext, "No Internet Connection");
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });


        holder.btn_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    //Utility.showToastShort(mContext, movie.getCartId() + " : " + holder.et_qty.getText().toString().trim());
                    updateCart(movie.getCartId(), holder.et_qty.getText().toString().trim());
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });


        holder.iv_sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_sub.isPressed()) {
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        if (amount > 0) {
                            amount -= 1;
                        }
                        if (amount == 0) {
                            amount = 1;
                        }
                        if (amount != 0) {
                            //   ActivityFavourites.count_list.add(amount);
                        }
                        holder.et_qty.setText(String.valueOf(amount));
//                    double tp=Double.parseDouble(moviesList.get(position).getPrice()) * amount;
                        holder.et_qty.setText(String.valueOf(amount));
                        Log.d("Position-", "" + holder.tv_position.getText().toString());
                       /* moviesList.set(position, new MyCart(moviesList.get(position).getPosition(), moviesList.get(position).getCart_id(),
                                moviesList.get(position).getThumb(),
                                moviesList.get(position).getId(), moviesList.get(position).getName(), moviesList.get(position).getModel(),
                                String.valueOf(amount), String.valueOf(amount), moviesList.get(position).getTotal()));*/
                        /*preparejson();*/
                    }
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });

        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnected()) {
                    if (holder.iv_add.isPressed()) {
                        if (!holder.et_qty.getText().toString().isEmpty()) {
                            amount = Integer.parseInt(holder.et_qty.getText().toString());
                        } else {
                            amount = 0;
                        }
                        amount += 1;
                        holder.et_qty.setText(String.valueOf(amount));
                        if (amount != 0) {
                            //ActivityFavourites.count_list.add(amount);
                        }
                        Log.d("Position+", "" + holder.tv_position.getText().toString());
                       /* moviesList.set(position, new MyCart(moviesList.get(position).getPosition(), moviesList.get(position).getCart_id(), moviesList.get(position).getThumb(),
                                moviesList.get(position).getId(), moviesList.get(position).getName(), moviesList.get(position).getModel(), String.valueOf(amount), moviesList.get(position).getPrice(), moviesList.get(position).getTotal()));*/
                        // preparejson();
                    }
                } else {
                    Utility.showToastShort(mContext, "No Internet Connection");
                }
            }
        });
    }

   /* private void preparejson() {
        try {
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put("user_id", Eutils.getUserid(mContext));
            JSONObject productValueObject = new JSONObject();
            JSONArray Dataarray = new JSONArray();
            for (int i = 0; i < moviesList.size(); i++) {
                String a = moviesList.get(i).getCart_id();
                String b = moviesList.get(i).getQuantity();
                if (!b.isEmpty() && !b.equals("0")) {
                    JSONObject projectObj = new JSONObject();
                    projectObj.put("cart_id", a);
                    projectObj.put("quantity", b);
                    productValueObject.put(b, projectObj);
                    Dataarray.put(i, projectObj);
                }
            }
            jsonObject.put("quantity", Dataarray);
            cartstringjson = jsonObject.toString().replaceAll("null,", "");
            Log.d("jsonObject", jsonObject.toString().replaceAll("null,", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postcartdata();
    }*/

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    private void postcartdata() {
        progressDialog.show();
       /* String cartedit = mContext.getString(R.string.cartedit) + "&device_id=" + Eutils.getdeviceid(mContext) + "&customer_id=" + Eutils.getUserid(mContext);
        Log.d("cartadddel=", cartedit);
        StringRequest postRequest = new StringRequest(Request.Method.POST, cartedit,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response :" + response.toString());
                        Log.d("Response", "" + response.toString());
                        progressDialog.dismiss();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            if (status.equals("1")) {
                                if (!Eutils.getLangPref(mContext).equals("ar")) {
                                    Eutils.showToastShort(mContext, "You have modified your shopping cart!");
                                } else {
                                    Eutils.showToastShort(mContext, "لقد قمت بتعديل سلة التسوق الخاصة بك!");
                                }
                                ((ActivityCart) mContext).download();
                            } else {
                                Eutils.showToastShort(mContext, "Please Try Again!!");
                            }
                        } catch (Exception e) {
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        SweetDialog.stopLoader();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("quantity", cartstringjson);
                Log.d("Poastdatta", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(mContext).add(postRequest);*/
    }


    private void deletefromcart1() {
        progressDialog.show();
        /*String removefromcart = mContext.getString(R.string.removefromcart) + "&device_id=" + Eutils.getdeviceid(mContext) + "&customer_id=" + Eutils.getUserid(mContext);
        Log.d("removefromcart=", removefromcart);
        StringRequest postRequest = new StringRequest(Request.Method.POST, removefromcart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response :" + response.toString());
                        Log.d("Response", "" + response.toString());
                        progressDialog.dismiss();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            if (status.equals("1")) {
                                if (!Eutils.getLangPref(mContext).equals("ar")) {
                                    Eutils.showToastShort(mContext, "You have modified your shopping cart!");
                                } else {
                                    Eutils.showToastShort(mContext, "لقد قمت بتعديل سلة التسوق الخاصة بك!");
                                }
                                ((ActivityCart) mContext).download();
                            } else {
                                Eutils.showToastShort(mContext, "Please Try Again!!");
                            }
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        SweetDialog.stopLoader();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", cart_id.trim());
                Log.d("Poastdatta", params.toString());
                return params;
            }
        };
        Volley.newRequestQueue(mContext).add(postRequest);*/
    }


    private void deletefromcart(String cart_id) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.GetCartDeleteAction(Preferences.get_userId(mContext), cart_id, Preferences.get_UniqueId(mContext));
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        ((ProductCart) mContext).LoadCartProduct();
                    } else {
                        Utility.showToastShort(mContext, cartDeleteAction.getMsg());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void updateCart(String cart_id, String quantity) {
        progressDialog.show();
        String BASE_URL = mContext.getResources().getString(R.string.base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices redditAPI;
        redditAPI = retrofit.create(ApiServices.class);
        Call<CartDeleteAction> call = redditAPI.UpdateMyCart(Preferences.get_userId(mContext), cart_id, Preferences.get_UniqueId(mContext), quantity);
        call.enqueue(new Callback<CartDeleteAction>() {

            @Override
            public void onResponse(Call<CartDeleteAction> call, retrofit2.Response<CartDeleteAction> response) {
                Log.d("String", "" + response);
                if (response.isSuccessful()) {
                    cartDeleteAction = response.body();
                    if (cartDeleteAction.getAck().equals("1")) {
                        Utility.showToastShort(mContext, cartDeleteAction.getMsg());
                        ((ProductCart) mContext).LoadCartProduct();
                    } else {
                        Utility.showToastShort(mContext, cartDeleteAction.getMsg());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDeleteAction> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}
