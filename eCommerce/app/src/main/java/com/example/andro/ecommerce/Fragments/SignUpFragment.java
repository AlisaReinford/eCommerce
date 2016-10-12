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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andro on 2016/10/11.
 */

public class SignUpFragment extends Fragment {

    public final static String url = "http://rjtmobile.com/ansari/shop_reg.php";

    EditText emailEditText;
    EditText passwordEditText;
    Button singnUpButton;
    ProgressDialog pDialog;

    public SignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        final EditText emailEditText = (EditText) rootView.findViewById(R.id.etEmail_sign_up);
        final EditText passwordEditText = (EditText) rootView.findViewById(R.id.etPassword_sign_up);

        final Button signUpButton = (Button) rootView.findViewById(R.id.btn_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();


            }
        });

        final Button singUpButton = (Button) rootView.findViewById(R.id.btn_trans_sign_in);
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                        .replace(R.id.container, new LoginFragment())
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
        singnUpButton.setEnabled(false);

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        pDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Waiting...");
        pDialog.show();
        String name = "";
        String email = emailEditText.getText().toString();
        String mobile = "";
        String password = passwordEditText.getText().toString();
        String query = url + "?name=" + name + "&email=" + email + "&mobile=" + mobile +
                "&password=" + password;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.PUT,
                query,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String flag = response.toString();
                            if (flag.equals("successfully registered")) {
                                pDialog.dismiss();
                                singnUpButton.setEnabled(true);
                                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                pDialog.hide();
                                onSignUpFailed();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // hide the progress dialog
                        Log.i("lihang", error.getMessage());
                        pDialog.hide();
                    }
                });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void onSignUpFailed() {
        Toast.makeText(getContext(), "Login failed, Please check your phone number and password", Toast.LENGTH_LONG).show();
        singnUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty()) {
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
