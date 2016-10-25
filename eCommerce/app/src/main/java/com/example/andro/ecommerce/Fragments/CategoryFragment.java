package com.example.andro.ecommerce.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.andro.ecommerce.Activity.ProductsActivity;
import com.example.andro.ecommerce.Adapter.SubCategoryAdapter;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.data.SubCategoryClickListener;
import com.example.andro.ecommerce.data.SubCategory;
import com.example.andro.ecommerce.utils.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andro on 2016/10/12.
 */

public class CategoryFragment extends Fragment implements SubCategoryClickListener {

    public final static String TAG = CategoryFragment.class.getName();

    public final static String url = "http://rjtmobile.com/ansari/shopingcart/androidapp/cust_sub_category.php";

    ArrayList<SubCategory> subCategories;
    String Id;
    SubCategoryAdapter subCategoryAdapter;
    PullRefreshLayout pullRefreshLayout;

    public CategoryFragment() {

    }

    public void setCategory(String Id) {
        this.Id = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        subCategories = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        subCategoryAdapter = new SubCategoryAdapter(subCategories, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(subCategoryAdapter);

        pullRefreshLayout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                subCategories.clear();
                getSubCategories();
            }
        });
        pullRefreshLayout.setRefreshing(true);
        getSubCategories();
        return rootView;
    }

    void getSubCategories() {
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
                        Log.i("Lihang", response.toString());
                        Log.d(TAG, response.toString());
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("SubCategory");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                SubCategory subCategory = new SubCategory();

                                subCategory.setId(object.getString("Id"));
                                subCategory.setSubCategoryName(object.getString("SubCatagoryName"));
                                subCategory.setSubCategoryDiscription(object.getString("SubCatagoryDiscription"));
                                subCategory.setCategoryImage(object.getString("CatagoryImage").trim());

                                subCategories.add(subCategory);
                            }

                        } catch (JSONException e) {
                            pullRefreshLayout.setRefreshing(false);
                            e.printStackTrace();
                        }
                        subCategoryAdapter.notifyDataSetChanged();
                        pullRefreshLayout.setRefreshing(false);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pullRefreshLayout.setRefreshing(false);
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onClick(SubCategory subCategory) {
        Intent intent = new Intent(getActivity(), ProductsActivity.class);
        intent.putExtra("subCategory", subCategory);
        startActivity(intent);
    }
}
