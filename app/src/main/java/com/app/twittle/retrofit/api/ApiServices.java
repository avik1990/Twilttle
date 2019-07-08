package com.app.twittle.retrofit.api;


import com.app.twittle.OrderCancellation;
import com.app.twittle.model.AboutUsmodel;
import com.app.twittle.model.AddToCart;
import com.app.twittle.model.CartDeleteAction;
import com.app.twittle.model.Category;
import com.app.twittle.model.ContactUsModel;
import com.app.twittle.model.MyCart;
import com.app.twittle.model.OrderCancellationModel;
import com.app.twittle.model.Privacymodel;
import com.app.twittle.model.ProductModel;
import com.app.twittle.model.Registration;
import com.app.twittle.model.TermsConditionmodel;
import com.app.twittle.model.ZipCodeVerify;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("service.php?action=pet_category")
    Call<Category> Getpet_category(@Query("user_id") String user_id);

    @GET("service.php?action=pet_food_list")
    Call<ProductModel> Getpet_food_list(@Query("category_id") String category_id, @Query("user_id") String user_id);

    @GET("service.php?action=registration")
    Call<Registration> UserRegistration(@Query("name") String name, @Query("email") String email, @Query("phone") String phone);

    @GET("service.php?action=about_us")
    Call<AboutUsmodel> GetAboutUS();

    @GET("service.php?action=our_contacts")
    Call<ContactUsModel> GetContactUs();

    @GET("service.php?action=order_cancellation")
    Call<OrderCancellationModel> Getorder_cancellation();

    @GET("service.php?action=replacement")
    Call<OrderCancellationModel> Getorder_Replacement();

    @GET("service.php?action=refund")
    Call<OrderCancellationModel> Getorder_Refund();

    @GET("service.php?action=terms")
    Call<TermsConditionmodel> GetTermData();

    @GET("service.php?action=privacy")
    Call<Privacymodel> GetPrivacyData();

    @GET("service.php?action=shipping")
    Call<Privacymodel> GetshippingData();


    @GET("service.php?action=zipcode_availability")
    Call<ZipCodeVerify> VerifyZipCode(@Query("zip") String zip, @Query("user_id") String user_id, @Query("unique_id") String unique_id);

    @GET("service.php?action=add_to_cart")
    Call<AddToCart> AddToCart(@Query("user_id") String user_id, @Query("product_id") String product_id, @Query("packet_id") String packet_id, @Query("unique_id") String unique_id);

    @GET("service.php?action=view_cart")
    Call<MyCart> GetMyCart(@Query("user_id") String user_id, @Query("unique_id") String unique_id);

    @GET("service.php?action=delete_item")
    Call<CartDeleteAction> GetCartDeleteAction(@Query("user_id") String user_id, @Query("cart_id") String cart_id, @Query("unique_id") String unique_id);

    @GET("service.php?action=update_item")
    Call<CartDeleteAction> UpdateMyCart(@Query("user_id") String user_id, @Query("cart_id") String cart_id, @Query("unique_id") String unique_id, @Query("quantity") String quantity);


    @GET("service.php?action=registration")
    Call<CartDeleteAction> PostShipping(@Query("name") String name, @Query("email") String email, @Query("phone") String phone
            , @Query("address") String address
            , @Query("city") String city
            , @Query("state") String state
            , @Query("zip") String zip
            , @Query("user_id") String user_id
            , @Query("unique_id") String unique_id);

    @GET("service.php?action=order_thankyou")
    Call<CartDeleteAction> GetCartThankyouMessage(@Query("user_id") String user_id, @Query("unique_id") String unique_id);


    @GET("service.php?action=post_feedback")
    Call<CartDeleteAction> PostFeedback(@Query("user_id") String user_id, @Query("name") String name, @Query("email") String email, @Query("phone") String phone
            , @Query("comment") String comment);


    @GET("service.php?action=search_result")
    Call<ProductModel> Getsearch_food_list(@Query("category_id") String category_id, @Query("user_id") String user_id, @Query("unique_id") String unique_id, @Query("search_string") String search_string);

}
