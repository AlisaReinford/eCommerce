package com.example.andro.ecommerce.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.andro.ecommerce.Adapter.ShoppingCartAdapter;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.customfonts.MyTextView;
import com.example.andro.ecommerce.data.ChangeCartListener;
import com.example.andro.ecommerce.data.User;
import com.example.andro.ecommerce.utils.DividerItemDecoration;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

public class ShoppingCartActivity extends AppCompatActivity implements ChangeCartListener{

    String url = "http://rjtmobile.com/ansari/oms/orders.php?order_status=1&store_id=801&";

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId("AbQsPg8vSWKvJL_Tlq_qKO6_d7ddKxz4TUk2rn7BakmvNCDMYfvVNNAcpN8FTh3Qegf1HmOCgAZROkL2");

    SharedPreferences mSharedPreference;
    ArrayList<String> cart = new ArrayList<>();
    MyTextView mtv_edit, mtv_name, mtv_address, mtv_total2, mtv_pay, mtv_paypal;
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

        // start paypal servive
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


    }

    void initView() {
        mtv_edit = (MyTextView) findViewById(R.id.edit);
        mtv_name = (MyTextView) findViewById((R.id.mtv_name));
        mtv_address = (MyTextView) findViewById(R.id.mtv_address);
        mtv_total2 = (MyTextView) findViewById(R.id.mtv_total2);
        mtv_pay = (MyTextView) findViewById(R.id.mtv_pay);
        mtv_paypal = (MyTextView) findViewById(R.id.mtv_paypal);

        mtv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( cart.size() == 0) {
                    Toast.makeText(ShoppingCartActivity.this, "Your shopping cart is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO
                    Toast.makeText(ShoppingCartActivity.this, "pay successful", Toast.LENGTH_SHORT);
                    deleteCart();
                    finish();
                }

            }
        });

        mtv_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
                // Change PAYMENT_INTENT_SALE to
                //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
                //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
                //     later via calls from your server.

                PayPalPayment payment = new PayPalPayment(new BigDecimal(calculateTotal()), "USD", "goods",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(ShoppingCartActivity.this, PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                startActivityForResult(intent, 0);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.cart_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    Toast.makeText(this, "confirmed", Toast.LENGTH_SHORT).show();
                    deleteCart();
                    finish();
                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    Toast.makeText(this, "an extremely unlikely failure occurred", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
            Toast.makeText(this, "The user canceled.", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            Toast.makeText(this, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
