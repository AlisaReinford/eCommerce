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
import com.android.volley.toolbox.StringRequest;
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
    EditText nameEditText;
    EditText phoneEditText;
    Button signUpButton;
    ProgressDialog pDialog;

    public SignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        emailEditText = (EditText) rootView.findViewById(R.id.etEmail_sign_up);
        passwordEditText = (EditText) rootView.findViewById(R.id.etPassword_sign_up);
        nameEditText = (EditText) rootView.findViewById(R.id.etName);
        phoneEditText = (EditText) rootView.findViewById(R.id.etPhone);

        signUpButton = (Button) rootView.findViewById(R.id.btn_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        final Button singInButton = (Button) rootView.findViewById(R.id.btn_trans_sign_in);
        singInButton.setOnClickListener(new View.OnClickListener() {
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

    public void signUp() {
        if (!validate()) {
            return;
        }

        // Tag used to cancel the request
        String tag_str = "str_req";
        pDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Waiting...");
        pDialog.show();
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String mobile = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String query = url + "?name=" + name + "&email=" + email + "&mobile=" + mobile +
                "&password=" + password;
        StringRequest strReq = new StringRequest(
                Request.Method.PUT,
                query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String flag = response.trim();
                        try {
                            if (flag.equals("successfully registered")) {
                                pDialog.dismiss();
                                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                                intent.putExtra("mobile", phoneEditText.getText().toString());
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                if (flag.equals("Mobile Number already exsist")) {
                                    pDialog.hide();
                                    Toast.makeText(getContext(), "Mobile Number already exist", Toast.LENGTH_LONG).show();
                                } else {
                                    pDialog.hide();
                                    onSignUpFailed();
                                }
                            }
                        } catch (Exception e) {
                            pDialog.hide();
                            onSignUpFailed();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("lihang", error.getMessage());
                        // hide the progress dialog
                        pDialog.hide();
                        onSignUpFailed();
                    }
                });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_str);

    }

    public void onSignUpFailed() {
        Toast.makeText(getContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String mobile = phoneEditText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError("at least 3 characters");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            phoneEditText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phoneEditText.setError(null);
        }

        return valid;
    }
}
