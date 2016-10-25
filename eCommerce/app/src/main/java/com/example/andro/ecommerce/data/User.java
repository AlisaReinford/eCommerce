package com.example.andro.ecommerce.data;

/**
 * Created by andro on 2016/10/15.
 */

public class User {
    public static String name;
    public static String phone;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        User.phone = phone;
    }
}
