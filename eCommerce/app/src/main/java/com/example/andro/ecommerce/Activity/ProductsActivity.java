package com.example.andro.ecommerce.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andro.ecommerce.Adapter.ProductsAdapter;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.data.Product;
import com.example.andro.ecommerce.data.ProductClickListener;
import com.example.andro.ecommerce.data.SubCategory;
import com.example.andro.ecommerce.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity implements ProductClickListener{
    public final static String TAG = ProductsActivity.class.getName();

    public final static String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_product.php";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private ProductsAdapter productsAdapter;
    private ArrayList<Product> products = null;
    private SubCategory subCategory;
    String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.products_recycler);

        initAppToolBar();
        initDataAndView();
        getProducts(Id);
    }


    private void initAppToolBar() {

        subCategory = getIntent().getParcelableExtra("subCategory");
        Id = subCategory.getId();
        String subCategoryName = subCategory.getSubCategoryName();
        // String subCategroyDescription = subCategory.getSubCategoryDescription();
        // String subCategroyImage = subCategory.getCategoryImage();
        // mToolbar.setNavigationIcon();
        mToolbar.setTitle(subCategoryName);
        mToolbar.inflateMenu(R.menu.products);
        mToolbar.setNavigationIcon(R.drawable.backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductsActivity.this.finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_shopping_cart:
                        SharedPreferences mSharedPreference = getSharedPreferences("" + User.phone, Context.MODE_PRIVATE);
                        if (mSharedPreference == null) {
                            Toast.makeText(getApplicationContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, String> map =  (Map<String, String>) mSharedPreference.getAll();
                            if (map.size() == 0) {
                                Toast.makeText(getApplicationContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(ProductsActivity.this, ShoppingCartActivity.class);
                                startActivity(intent);
                            }

                        }

                }
                return true;
            }
        });
    }

    private void initDataAndView() {
        products = new ArrayList<>();
        productsAdapter = new ProductsAdapter(products);
        productsAdapter.setProductsClickListener(this);
        mRecyclerView.setAdapter(productsAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
    }

    void getProducts(String Id) {
        Log.i("lihang", Id);
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String query = url + "?Id=" + Id;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                query,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("lihang", response.toString());
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("Product");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Product product = new Product();
                                product.setId(object.getString("Id"));
                                product.setProductName(object.getString("ProductName"));
                                product.setQuantity(Integer.parseInt(object.getString("Quantity")));
                                product.setPrice(Float.parseFloat(object.getString("Prize")));
                                product.setDescription(object.getString("Discription"));
                                product.setImage(object.getString("Image"));

                                products.add(product);
                            }
                             productsAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(ProductsActivity.this, ProductInfoActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }
}
