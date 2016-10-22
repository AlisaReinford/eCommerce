package com.example.andro.ecommerce.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.customfonts.MyTextView;
import com.example.andro.ecommerce.data.Product;
import com.example.andro.ecommerce.data.User;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ProductInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = ProductInfoActivity.class.getName();

    MyTextView mtv_name, mtv_description, mtv_price;
    ImageView iv_back, iv_cart, iv_product_info, iv_addtocart;
    Product product;
    SharedPreferences mSharedPreference;
    ArrayList<String> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        product = getIntent().getParcelableExtra("product");

        initView();
        loadCart();

    }

    private void initView() {
        mtv_name = (MyTextView) findViewById(R.id.mtv_name);
        mtv_description = (MyTextView) findViewById(R.id.mtv_description);
        mtv_price = (MyTextView) findViewById(R.id.mtv_price);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        iv_product_info = (ImageView) findViewById(R.id.iv_product_info);
        iv_addtocart = (ImageView) findViewById(R.id.iv_addtocart);

        mtv_name.setText(product.getProductName());
        mtv_description.setText(product.getDescription());
        mtv_price.setText(String.valueOf(product.getPrice()));

        imageLoading(product.getImage(), iv_product_info);

        iv_back.setOnClickListener(this);
        iv_cart.setOnClickListener(this);
        iv_addtocart.setOnClickListener(this);
    }

    void loadCart() {
        mSharedPreference = getSharedPreferences("" + User.phone, Context.MODE_PRIVATE);
        if (mSharedPreference == null) {
            return;
        }
        cart.clear();
        // format: 1.Id 2.ProductName 3.Quantity 4.Price 5.Image Url
        Map<String, String> map =  (Map<String, String>) mSharedPreference.getAll();
        for (String s : map.keySet()) {
            cart.add(map.get(s));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cart:
                Map<String, String> map =  (Map<String, String>) mSharedPreference.getAll();
                if (map.size() == 0) {
                    Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProductInfoActivity.this, ShoppingCartActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_addtocart:
                for ( int i = 0; i < cart.size(); i++) {
                    String[] strs = cart.get(i).split(",");
                    if (strs[0].equals(product.getId())) {
                        strs[2] = (Integer.parseInt(strs[2]) + 1) + "";
                        SharedPreferences.Editor mEdit = mSharedPreference.edit();
                        mEdit.remove("cart_" + product.getId());
                        mEdit.putString("cart_" + product.getId(), strs[0] + "," + strs[1] + "," + strs[2] + "," + strs[3] + "," + strs[4]);
                        mEdit.commit();
                        Toast.makeText(this, "added to the shopping cart", Toast.LENGTH_SHORT).show();
                        loadCart();
                        Log.i("ooxx", "1   " + cart.toString());
                        return;
                    }
                }
                SharedPreferences.Editor mEdit = mSharedPreference.edit();
                mEdit.putString("cart_" + product.getId(), product.getId() + "," + product.getProductName() + "," +
                        1 + "," + product.getPrice() + "," + product.getImage());
                mEdit.commit();
                Toast.makeText(this, "added to the shopping cart", Toast.LENGTH_SHORT).show();
                loadCart();
                Log.i("ooxx", "2    " + cart.toString());
        }
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
