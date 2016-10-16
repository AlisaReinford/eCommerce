package com.example.andro.ecommerce.data;

/**
 * Created by andro on 2016/10/12.
 */

public class Category {
    String Id;
    String CatagoryName;
    String CatagoryDiscription;
    String CatagoryImage;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCatagoryName() {
        return CatagoryName;
    }

    public void setCatagoryName(String catagoryName) {
        CatagoryName = catagoryName;
    }

    public String getCatagoryDiscription() {
        return CatagoryDiscription;
    }

    public void setCatagoryDiscription(String catagoryDiscription) {
        CatagoryDiscription = catagoryDiscription;
    }

    public String getCatagoryImage() {
        return CatagoryImage;
    }

    public void setCatagoryImage(String catagoryImage) {
        CatagoryImage = catagoryImage;
    }
}
