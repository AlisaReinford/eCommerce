package com.example.andro.ecommerce.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.andro.ecommerce.Adapter.ShoppingCartAdapter;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.customfonts.MyTextView;
import com.example.andro.ecommerce.data.ChangeCartListener;
import com.example.andro.ecommerce.data.User;
import com.example.andro.ecommerce.utils.DividerItemDecoration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

public class ShoppingCartActivity extends AppCompatActivity implements ChangeCartListener{

    String url = "http://rjtmobile.com/ansari/oms/orders.php?order_status=1&store_id=801&";

    SharedPreferences mSharedPreference;
    ArrayList<String> cart = new ArrayList<>();
    MyTextView mtv_edit, mtv_name, mtv_address, mtv_total2, mtv_pay;
    RecyclerView recyclerView;
    ShoppingCartAdapter shoppingCartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shopping Cart");
        toolbar.setNavigationIcon(R.drawable.backarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.cart);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.clean_shopping_cart) {
                    deleteCart();
                }
                return true;
            }
        });


        loadCart();
        initView();


    }

    void initView() {
        mtv_edit = (MyTextView) findViewById(R.id.edit);
        mtv_name = (MyTextView) findViewById((R.id.mtv_name));
        mtv_address = (MyTextView) findViewById(R.id.mtv_address);
        mtv_total2 = (MyTextView) findViewById(R.id.mtv_total2);
        mtv_pay = (MyTextView) findViewById(R.id.mtv_pay);

        mtv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( cart.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Your shopping cart is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO
                    Toast.makeText(getApplicationContext(), "pay successful", Toast.LENGTH_SHORT);
                    deleteCart();
                    finish();
                }

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        shoppingCartAdapter = new ShoppingCartAdapter(this, this, cart);
        recyclerView.setAdapter(shoppingCartAdapter);


        mtv_total2.setText("$ " + calculateTotal());

    }

    String calculateTotal() {
        float total = 0;
        for (int i = 0; i < cart.size(); i++) {
            String[] strs = cart.get(i).split(",");
            total = total + Integer.parseInt(strs[2]) * Float.parseFloat(strs[3]);
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(total);

    }

    void loadCart() {
        mSharedPreference = getSharedPreferences("" + User.phone, Context.MODE_PRIVATE);
        cart.clear();
        int size = mSharedPreference.getInt("cart_size", 0);
        // format: 1.Id 2.ProductName 3.Quantity 4.Price 5.Image Url
        Map<String, String> map = (Map<String, String>) mSharedPreference.getAll();
        for (String s : map.keySet()) {
            cart.add(map.get(s));
        }
    }

    @Override
    public void onPlus() {
        loadCart();
        mtv_total2.setText("$ " + calculateTotal());
    }

    @Override
    public void onMin() {
        loadCart();
        mtv_total2.setText("$ " + calculateTotal());
    }

    void deleteCart() {

        SharedPreferences.Editor mEdit = mSharedPreference.edit();
        Map<String, String> map = (Map<String, String>)mSharedPreference.getAll();
        if (map.size() == 0) {
            Toast.makeText(getApplicationContext(), "Your shopping cart is empty.", Toast.LENGTH_SHORT).show();
        } else {
            mEdit.clear();
            mEdit.commit();
            cart.clear();
            mtv_total2.setText("$ " + calculateTotal());
            shoppingCartAdapter.notifyDataSetChanged();
        }
    }
}
