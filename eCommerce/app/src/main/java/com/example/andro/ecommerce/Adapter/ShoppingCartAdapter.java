package com.example.andro.ecommerce.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.customfonts.MyTextView;
import com.example.andro.ecommerce.data.ChangeCartListener;
import com.example.andro.ecommerce.data.User;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by andro on 2016/10/16.
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.MyViewHolder> {

    public final static String TAG = ProductsAdapter.class.getName();

    ArrayList<String> cart = new ArrayList<>();
    SharedPreferences mSharedPreference;
    Context context;
    ChangeCartListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_cart_product;
        public TextView tv_cart_name;
        public TextView tv_cart_price;
        public TextView tv_cart_quantity;


        public MyViewHolder(View itemView) {
            super(itemView);
            iv_cart_product = (ImageView) itemView.findViewById(R.id.iv_cart_product);
            tv_cart_name= (TextView) itemView.findViewById(R.id.tv_cart_name);
            tv_cart_price = (TextView) itemView.findViewById(R.id.tv_cart_price);
            tv_cart_quantity = (TextView) itemView.findViewById(R.id.tv_cart_quantity);
            itemView.findViewById(R.id.iv_plus).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] strs = cart.get(getAdapterPosition()).split(",");
                    strs[2] = (Integer.parseInt(strs[2]) + 1) + "";
                    SharedPreferences.Editor mEdit = mSharedPreference.edit();
                    mEdit.remove("cart_" + strs[0]);
                    mEdit.putString("cart_" + strs[0], strs[0] + "," + strs[1] + "," + strs[2] + "," + strs[3] + "," + strs[4]);
                    mEdit.commit();
                    loadCart();
                    notifyDataSetChanged();
                    listener.onPlus();
                }
            });
            itemView.findViewById(R.id.iv_min).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] strs = cart.get(getAdapterPosition()).split(",");
                    if (strs[2].equals("1")) {
                        SharedPreferences.Editor mEdit = mSharedPreference.edit();
                        mEdit.remove("cart_" + strs[0]);
                        mEdit.commit();
                        loadCart();
                        notifyDataSetChanged();
                        listener.onMin();
                    } else {
                        strs[2] = (Integer.parseInt(strs[2]) - 1) + "";
                        SharedPreferences.Editor mEdit = mSharedPreference.edit();
                        mEdit.remove("cart_" + strs[0]);
                        mEdit.putString("cart_" + strs[0], strs[0] + "," + strs[1] + "," + strs[2] + "," + strs[3] + "," + strs[4]);
                        mEdit.commit();
                        loadCart();
                        notifyDataSetChanged();
                        listener.onMin();
                    }
                }
            });
        }
    }

    public ShoppingCartAdapter(Context context, ChangeCartListener listener, ArrayList<String> cart) {
        this.context = context;
        this.listener = listener;
        this.cart = cart;
        mSharedPreference = context.getSharedPreferences("" + User.phone, Context.MODE_PRIVATE);
    }

    void loadCart() {
        mSharedPreference = context.getSharedPreferences("" + User.phone, Context.MODE_PRIVATE);
        if (mSharedPreference == null) {
            return;
        }
        cart.clear();

        // format: 1.Id 2.ProductName 3.Quantity 4.Price 5.Image Url
        Map<String, String> map =  (Map<String, String>) mSharedPreference.getAll();
        for (String s : map.keySet()) {
            cart.add(s);
        }
    }


    @Override
    public ShoppingCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list_item, parent, false);
        return new ShoppingCartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShoppingCartAdapter.MyViewHolder holder, int position) {
        String[] strs = cart.get(position).split(",");
        imageLoading(strs[4], holder.iv_cart_product);
        holder.tv_cart_name.setText(strs[1]);
        holder.tv_cart_price.setText("$ " + strs[3]);
        holder.tv_cart_quantity.setText(strs[2]);

    }

    @Override
    public int getItemCount() {
        return cart.size();
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
