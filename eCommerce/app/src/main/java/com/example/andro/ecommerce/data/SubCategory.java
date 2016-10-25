package com.example.andro.ecommerce.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andro on 2016/10/13.
 */

public class SubCategory implements Parcelable{
    String Id;
    String SubCategoryName;
    String SubCategoryDiscription;
    String CategoryImage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryDescription() {
        return SubCategoryDiscription;
    }

    public void setSubCategoryDiscription(String subCategoryDiscription) {
        SubCategoryDiscription = subCategoryDiscription;
    }
    
    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(SubCategoryName);
        dest.writeString(SubCategoryDiscription);
        dest.writeString(CategoryImage);
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel source) {
            SubCategory subCategory = new SubCategory();
            subCategory.setId(source.readString());
            subCategory.setSubCategoryName(source.readString());
            subCategory.setSubCategoryDiscription(source.readString());
            subCategory.setCategoryImage(source.readString());
            return subCategory;
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[0];
        }
    };

}
