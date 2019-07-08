package com.app.twittle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avik on 27-08-2017.
 */

public class MyCart {

    public List<String> total_arr;
    @SerializedName("CartData")
    @Expose
    public List<CartDatum> cartData = null;
    @SerializedName("PriceData")
    @Expose
    public PriceData priceData;
    @SerializedName("Ack")
    @Expose
    public String ack;
    @SerializedName("msg")
    @Expose
    public String msg;

    public List<CartDatum> getCartData() {
        return cartData;
    }

    public void setCartData(List<CartDatum> cartData) {
        this.cartData = cartData;
    }

    public PriceData getPriceData() {
        return priceData;
    }

    public void setPriceData(PriceData priceData) {
        this.priceData = priceData;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class PriceData {

        @SerializedName("total_price")
        @Expose
        public String totalPrice;
        @SerializedName("service_tax_amount")
        @Expose
        public String serviceTaxAmount;
        @SerializedName("service_tax_percentage")
        @Expose
        public String serviceTaxPercentage;
        @SerializedName("grand_total")
        @Expose
        public String grandTotal;
        @SerializedName("total_quantity")
        @Expose
        public String total_quantity;

        public String getTotal_quantity() {
            return total_quantity;
        }

        public void setTotal_quantity(String total_quantity) {
            this.total_quantity = total_quantity;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getServiceTaxAmount() {
            return serviceTaxAmount;
        }

        public void setServiceTaxAmount(String serviceTaxAmount) {
            this.serviceTaxAmount = serviceTaxAmount;
        }

        public String getServiceTaxPercentage() {
            return serviceTaxPercentage;
        }

        public void setServiceTaxPercentage(String serviceTaxPercentage) {
            this.serviceTaxPercentage = serviceTaxPercentage;
        }

        public String getGrandTotal() {
            return grandTotal;
        }

        public void setGrandTotal(String grandTotal) {
            this.grandTotal = grandTotal;
        }

    }

    public class CartDatum {

        @SerializedName("cart_id")
        @Expose
        public String cartId;
        @SerializedName("product_photo")
        @Expose
        public String productPhoto;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("packet_size")
        @Expose
        public String packetSize;
        @SerializedName("unit_price")
        @Expose
        public String unitPrice;
        @SerializedName("subtotal")
        @Expose
        public String subtotal;
        @SerializedName("quantity")
        @Expose
        public String quantity;

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getProductPhoto() {
            return productPhoto;
        }

        public void setProductPhoto(String productPhoto) {
            this.productPhoto = productPhoto;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPacketSize() {
            return packetSize;
        }

        public void setPacketSize(String packetSize) {
            this.packetSize = packetSize;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

    }


}
