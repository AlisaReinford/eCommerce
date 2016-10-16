package com.example.andro.ecommerce.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andro on 2016/10/13.
 */

public class SubCategory implements Parcelable{
    String Id;
    String SubCatagoryName;
    String SubCatagoryDiscription;
    String CatagoryImage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSubCatagoryName() {
        return SubCatagoryName;
    }

    public void setSubCatagoryName(String subCatagoryName) {
        SubCatagoryName = subCatagoryName;
    }

    public String getSubCatagoryDiscription() {
        return SubCatagoryDiscription;
    }

    public void setSubCatagoryDiscription(String subCatagoryDiscription) {
        SubCatagoryDiscription = subCatagoryDiscription;
    }

    public String getCatagoryImage() {
        return CatagoryImage;
    }

    public void setCatagoryImage(String catagoryImage) {
        CatagoryImage = catagoryImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(SubCatagoryName);
        dest.writeString(SubCatagoryDiscription);
        dest.writeString(CatagoryImage);
    }

    public static final Creator<SubCategory> CREATOR = new Creator<SubCategory>() {
        @Override
        public SubCategory createFromParcel(Parcel source) {
            SubCategory subCategory = new SubCategory();
            subCategory.setId(source.readString());
            subCategory.setSubCatagoryName(source.readString());
            subCategory.setSubCatagoryDiscription(source.readString());
            subCategory.setCatagoryImage(source.readString());
            return subCategory;
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[0];
        }
    };

}
