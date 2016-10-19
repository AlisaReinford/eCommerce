package com.example.andro.ecommerce.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.example.andro.ecommerce.Fragments.CategoryFragment;
import com.example.andro.ecommerce.Fragments.SignUpFragment;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.data.Category;

import java.util.ArrayList;

/**
 * Created by andro on 2016/10/12.
 */

public class CategoryPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Category> categories;

    public CategoryPagerAdapter(FragmentManager fm, ArrayList<Category> categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {

        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setCategory(categories.get(position).getId());

        return categoryFragment;
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return categories.size();
    }


}
