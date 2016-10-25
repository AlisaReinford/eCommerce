package com.example.andro.ecommerce.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.data.SubCategoryClickListener;
import com.example.andro.ecommerce.data.SubCategory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andro on 2016/10/13.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder>{

    public final static String TAG = SubCategoryAdapter.class.getName();

    ArrayList<SubCategory> subCategories;
    SubCategoryClickListener listener;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_sub;
        public TextView tv_name;
        public TextView tv_des;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_sub = (ImageView) itemView.findViewById(R.id.iv_sub);
            tv_name = (TextView) itemView.findViewById(R.id.tv_sub_name);
            tv_des = (TextView) itemView.findViewById(R.id.tv_sub_des);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(subCategories.get(getAdapterPosition()));
                }
            });
        }
    }

    public SubCategoryAdapter(ArrayList<SubCategory> subCategories, SubCategoryClickListener listener) {
        this.subCategories = subCategories;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCategory subCategory= subCategories.get(position);
        imageLoading(subCategory.getCategoryImage(), holder.iv_sub);
        holder.tv_name.setText(subCategories.get(position).getSubCategoryName());
        holder.tv_des.setText(subCategories.get(position).getSubCategoryDescription());
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    private void imageLoading(String url, final ImageView imageView) {
        try {
            URL l = new URL(url);
            URI i = new URI(l.getProtocol(), l.getUserInfo(), l.getHost(), l.getPort(), l.getPath(), l.getQuery(), l.getRef());
            l = i.toURL();
            ImageLoader imageLoader = AppController.getInstance().getmImageLoader();

            // If you are using normal ImageView
            imageLoader.get(l.toString(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Image Load Error: " + error.getMessage());
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
