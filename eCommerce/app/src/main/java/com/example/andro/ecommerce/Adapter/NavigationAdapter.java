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
import com.example.andro.ecommerce.data.Category;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andro on 2016/10/13.
 */

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.MyViewHolder> {
    public final static String TAG = NavigationAdapter.class.getName();

    ArrayList<Category> categories;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_category;
        public TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_category = (ImageView) itemView.findViewById(R.id.iv_nav);
            tv_name = (TextView) itemView.findViewById(R.id.tv_nav);
        }
    }

    public NavigationAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public NavigationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_list_item, parent, false);
        return new NavigationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = categories.get(position);
        imageLoading(category.getCatagoryImage(), holder.iv_category);
        holder.tv_name.setText(category.getCatagoryName());
    }


    @Override
    public int getItemCount() {
        return categories.size();
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
