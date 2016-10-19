package com.example.andro.ecommerce.Activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = ProductInfoActivity.class.getName();

    RelativeLayout rl;
    MyTextView mtv_name, mtv_description, mtv_price;
    ImageView iv_back, iv_cart, iv_product_info, iv_addtocart;
    Product product;
    SharedPreferences mSharedPreference;
    ArrayList<String> cart = new ArrayList<>();
    private PathMeasure mPathMeasure;
    /**
     * the coordinate of middle point
     */
    private float[] mCurrentPosition = new float[2];
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        product = getIntent().getParcelableExtra("product");

        initView();
        loadCart();

    }

    private void initView() {
        rl = (RelativeLayout) findViewById(R.id.rl);
        mtv_name = (MyTextView) findViewById(R.id.mtv_name);
        mtv_description = (MyTextView) findViewById(R.id.mtv_description);
        mtv_price = (MyTextView) findViewById(R.id.mtv_price);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        iv_product_info = (ImageView) findViewById(R.id.iv_product_info);
        iv_addtocart = (ImageView) findViewById(R.id.iv_addtocart);

        mtv_name.setText(product.getProductName());
        mtv_description.setText(product.getDescription());
        mtv_price.setText("$ " + product.getPrice());

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
                addToCartAnimation(iv_addtocart);
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
                        return;
                    }
                }
                SharedPreferences.Editor mEdit = mSharedPreference.edit();
                mEdit.putString("cart_" + product.getId(), product.getId() + "," + product.getProductName() + "," +
                        1 + "," + product.getPrice() + "," + product.getImage());
                mEdit.commit();
                Toast.makeText(this, "added to the shopping cart", Toast.LENGTH_SHORT).show();
                loadCart();
        }
    }

    // animation when you add goods to the shopping cart
    void addToCartAnimation(ImageView iv) {

        // 1.create a new imageview to execute the animation
        final CircleImageView goods = new CircleImageView(ProductInfoActivity.this);
        goods.setBorderColor(getResources().getColor(R.color.splashBkg));
        goods.setBorderWidth(1);
        goods.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        rl.addView(goods, params);

        // 2.the preparation of calculating the start/end coordinate
        // get the parent start coordinate
        int[] parentLocation = new int[2];
        rl.getLocationInWindow(parentLocation);

        // get the goods icon coordinate
        int[] startLoc = new int[2];
        iv.getLocationInWindow(startLoc);

        // get the shopping cart icon coordinate;
        int[] endLoc = new int[2];
        iv_cart.getLocationInWindow(endLoc);

        // 3.start calculating start/end coordinate
        // start coordinate
        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + iv.getHeight() / 2;

        // end coordinate
        float toX = endLoc[0] - parentLocation[0] + iv_cart.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        // 4.calculate the interpolation coordinate of middle animation
        // start drawing Bezier Curve
        Path path = new Path();
        // move to start point
        path.moveTo(startX, startY);
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        mPathMeasure = new PathMeasure(path, false);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // boolean getPosTan(float distance, float[] pos, float[] tan)
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });

        // start executing animation
        valueAnimator.start();

        // operation after animation
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rl.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
                        bitmap = response.getBitmap();
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
