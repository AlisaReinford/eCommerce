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
import com.example.andro.ecommerce.data.Product;
import com.example.andro.ecommerce.data.ProductClickListener;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by andro on 2016/10/14.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>{

    public final static String TAG = ProductsAdapter.class.getName();

    ArrayList<Product> products;
    ProductClickListener productClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_product;
        public TextView tv_product_name;
        public TextView tv_product_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
            tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            tv_product_price = (TextView) itemView.findViewById(R.id.tv_product_price);
            if(productClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productClickListener.onClick(products.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    public void setProductsClickListener(ProductClickListener productClickListener) {
        this.productClickListener = productClickListener;
    }

    public ProductsAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pro_list_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = products.get(position);
        imageLoading(product.getImage(), holder.iv_product);
        holder.tv_product_name.setText(product.getProductName());
        holder.tv_product_price.setText("$ " + String.valueOf(product.getPrice()));

    }

    @Override
    public int getItemCount() {
        return products.size();
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
