package com.app.twittle.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.twittle.R;
import com.app.twittle.model.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Avik on 11-01-2017.
 */
public class ProdductAdapter extends RecyclerView.Adapter<ProdductAdapter.MyViewHolder> {

    private List<ProductModel.ProductDatum> countryList;
    Context mContext;
    AdapterPos adapterPos;

    public interface AdapterPos {
        void adapterPosition(int pos);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_chefimage;
        TextView tv_productname, tv_position;
        ProgressBar progressbar;
        CardView card_view;
        Button btn_details;


        public MyViewHolder(View view) {
            super(view);
            iv_chefimage = (ImageView) view.findViewById(R.id.iv_chefimage);
            tv_productname = (TextView) view.findViewById(R.id.tv_productname);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
            card_view = (CardView) view.findViewById(R.id.card_view);
            tv_position = view.findViewById(R.id.tv_position);
            btn_details = view.findViewById(R.id.btn_details);
        }
    }

    public ProdductAdapter(Context mContext, List<ProductModel.ProductDatum> countryList, AdapterPos adapterPos) {
        this.mContext = mContext;
        this.countryList = countryList;
        this.adapterPos = adapterPos;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductModel.ProductDatum c = countryList.get(position);
        holder.tv_productname.setText(c.getProductName());
        holder.progressbar.setVisibility(View.VISIBLE);
        holder.tv_position.setText(String.valueOf(position));
        holder.iv_chefimage.setVisibility(View.VISIBLE);
        final ProgressBar progressView = holder.progressbar;
        try {
            Picasso.with(mContext)
                    .load(c.getProductPhoto())
                    .into(holder.iv_chefimage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressView.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {
        }

        holder.btn_details.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                adapterPos.adapterPosition(Integer.parseInt(holder.tv_position.getText().toString()));
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("RV", "Item size [" + countryList.size() + "]");
        return countryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products, parent, false);
        return new MyViewHolder(v);
    }
}
