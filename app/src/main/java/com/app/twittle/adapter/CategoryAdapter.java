package com.app.twittle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.twittle.ProductPage;
import com.app.twittle.R;
import com.app.twittle.model.Category;
import com.app.twittle.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Avik on 11-01-2017.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category.CategoryDatum> countryList;
    Context mContext;

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_catimage;
        TextView tv_categoryname;
        ProgressBar progressbar;
        RelativeLayout card_view;


        public MyViewHolder(View view) {
            super(view);
            iv_catimage = (ImageView) view.findViewById(R.id.iv_catimage);
            tv_categoryname = (TextView) view.findViewById(R.id.tv_categoryname);
            progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
            card_view = (RelativeLayout) view.findViewById(R.id.card_view);
        }
    }

    public CategoryAdapter(Context mContext, List<Category.CategoryDatum> countryList) {
        this.mContext = mContext;
        this.countryList = countryList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Category.CategoryDatum c = countryList.get(position);
        holder.progressbar.setVisibility(View.VISIBLE);
        holder.iv_catimage.setVisibility(View.VISIBLE);
        holder.tv_categoryname.setText(c.getCategoryName());
        final ProgressBar progressView = holder.progressbar;
        Picasso.with(mContext)
                .load(c.getCategoryPhoto())
                .into(holder.iv_catimage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.card_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductPage.class);
                i.putExtra("from", countryList.get(position).getCategoryName());
                i.putExtra("cat_id", countryList.get(position).getCategoryId());
                mContext.startActivity(i);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(v);
    }
}
