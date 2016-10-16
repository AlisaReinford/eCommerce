package com.example.andro.ecommerce.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andro.ecommerce.Adapter.CategoryPagerAdapter;
import com.example.andro.ecommerce.Adapter.ShoppingCartAdapter;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.data.Category;
import com.example.andro.ecommerce.data.User;
import com.example.andro.ecommerce.utils.ImageUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static String TAG = CategoryActivity.class.getName();

    public final static String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_category.php";

    ArrayList<Category> categories;
    CategoryPagerAdapter pagerAdapter;
    private float slideMenuWidth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categories = new ArrayList<>();
        getCategories();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        slideMenuWidth = getResources().getDimension(R.dimen.navigation_drawer_width);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView tv_mobile = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_mobile);
        tv_mobile.setText("Hi, " + getIntent().getStringExtra("mobile"));

       /* navigationView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (bitmap == null) {
                    drawerLayout.buildDrawingCache();
                    bitmap = drawerLayout.getDrawingCache();
                }
                int[] location = new int[2];
                navigationView.getLocationInWindow(location);
                blur(bitmap, navigationView, location[0]);
                return true;
            }
        });*/
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void blur(Bitmap bkg, View view, int width) {
        float scaleFactor = 4;
        float radius = 35;

        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth() / scaleFactor),
                (int)(view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        float visibleWidth = slideMenuWidth + width;
        int visibleHeight = view.getHeight();
        Rect src = new Rect(0, 0, (int)(visibleWidth) + 10, visibleHeight);
        RectF dest = new RectF(0, 0, (int)(visibleWidth) + 10, visibleHeight);
        canvas.drawBitmap(bkg, src, dest, paint);
        overlay = ImageUtils.doBlur(overlay, (int)radius, true);
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));
        } else {
            view.setBackground(new BitmapDrawable(getResources(), overlay));
        }

    }

    private void getCategories() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("Category");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                Category category = new Category();
                                category.setId(object.getString("Id"));
                                category.setCatagoryName(object.getString("CatagoryName"));
                                category.setCatagoryDiscription(object.getString("CatagoryDiscription"));
                                category.setCatagoryImage(object.getString("CatagoryImage"));

                                categories.add(category);
                            }
                            setTab();
                           // pagerAdapter.notifyDataSetChanged();

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

    private void setTab() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        for (int i = 0; i < categories.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(categories.get(i).getCatagoryName()));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CategoryPagerAdapter(
                getSupportFragmentManager(), categories);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            final NiftyDialogBuilder dialogBuilder1 = NiftyDialogBuilder.getInstance(this);
            dialogBuilder1
                    .withTitle("eCommerce")
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withMessage("Lihang Gu\nVersion:1.0")
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor(R.color.colorblue)
                    .withIcon(R.drawable.app_icon)
                    .withButton1Text("OK")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder1.dismiss();
                        }
                    })
                    .show();
            return true;
        }

        if(id== R.id.action_shopping_cart) {
            SharedPreferences mSharedPreference = getSharedPreferences("" + User.phone, Context.MODE_PRIVATE);
            if (mSharedPreference == null) {
                Toast.makeText(getApplicationContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, String> map =  (Map<String, String>) mSharedPreference.getAll();
                if (map.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CategoryActivity.this, ShoppingCartActivity.class);
                    startActivity(intent);
                }

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_checkout:
                Intent intent1 = new Intent(CategoryActivity.this, ShoppingCartActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_info:
                final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(this);
                dialogBuilder
                        .withTitle("User Information")
                        .withTitleColor("#FFFFFF")
                        .withDividerColor("#11000000")
                        .withMessage("Mobile: " + User.phone)
                        .withMessageColor("#FFFFFFFF")
                        .withDialogColor(R.color.colorblue)
                        .withIcon(R.drawable.app_icon)
                        .withButton1Text("OK")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.nav_logout:
                Intent intent2 = new Intent(CategoryActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_exit:
                finish();
                break;
            case R.id.nav_about:
                final NiftyDialogBuilder dialogBuilder1 = NiftyDialogBuilder.getInstance(this);
                dialogBuilder1
                        .withTitle("eCommerce")
                        .withTitleColor("#FFFFFF")
                        .withDividerColor("#11000000")
                        .withMessage("Lihang Gu\nVersion:1.0")
                        .withMessageColor("#FFFFFFFF")
                        .withDialogColor(R.color.colorblue)
                        .withIcon(R.drawable.app_icon)
                        .withButton1Text("OK")
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder1.dismiss();
                            }
                        })
                        .show();
                break;

        }

        if (id == R.id.nav_logout) {
            Intent intent = new Intent(CategoryActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
