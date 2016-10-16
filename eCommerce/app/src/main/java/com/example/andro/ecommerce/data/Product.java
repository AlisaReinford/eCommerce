package com.example.andro.ecommerce.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andro on 2016/10/12.
 */

public class Product implements Parcelable{
    String Id;
    String ProductName;
    int Quantity;
    float Price;
    String Description;
    String Image;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(Id);
        dest.writeString(ProductName);
        dest.writeInt(Quantity);
        dest.writeFloat(Price);
        dest.writeString(Description);
        dest.writeString(Image);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            Product product = new Product();
            product.setId(source.readString());
            product.setProductName(source.readString());
            product.setQuantity(source.readInt());
            product.setPrice(source.readFloat());
            product.setDescription(source.readString());
            product.setImage(source.readString());
            return product;
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[0];
        }
    };
}
