package com.example.andro.ecommerce.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.andro.ecommerce.Activity.CategoryActivity;
import com.example.andro.ecommerce.R;
import com.example.andro.ecommerce.app.AppController;
import com.example.andro.ecommerce.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andro on 2016/10/11.
 */

public class LoginFragment extends Fragment {

    public final static String url = "http://rjtmobile.com/ansari/shop_login.php";

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressDialog pDialog;



    public LoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        emailEditText = (EditText) rootView.findViewById(R.id.etEmail);
        passwordEditText = (EditText) rootView.findViewById(R.id.etPassword);

        loginButton = (Button) rootView.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        final Button singUpButton = (Button) rootView.findViewById(R.id.btn_trans_sign_up);
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                        .replace(R.id.container, new SignUpFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return rootView;
    }

    public void login() {
        if (!validate()) {
            return;
        }


        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        pDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Authenticating...");
        pDialog.show();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String query = url + "?mobile=" + email + "&password=" + password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                query,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray;
                        try {
                            jsonArray = response.getJSONArray("msg");
                            String flag = jsonArray.get(0).toString();
                            if (flag.equals("success")) {
                                pDialog.dismiss();
                                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                                intent.putExtra("mobile", jsonArray.get(1).toString());
                                User.phone = jsonArray.get(1).toString();
                                getActivity().finish();
                                startActivity(intent);

                            } else {
                                if (flag.equals("failure")) {
                                    Toast.makeText(getContext(), "Please check your email and password", Toast.LENGTH_LONG).show();
                                    pDialog.dismiss();

                                } else {
                                    pDialog.dismiss();
                                    onLoginFailed();
                                }

                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            onLoginFailed();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        onLoginFailed();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void onLoginFailed() {
        Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String mobile = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (mobile.isEmpty() || mobile.length() != 10) {
            emailEditText.setError("enter a valid email address");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordEditText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }


}
