package com.nacoda.wisata.presenter;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nacoda.wisata.utilities.URL;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ghifari on 9/6/17.
 */

public class Presenter {


    private PresenterInterface presenterInterface;

    public Presenter(PresenterInterface presenterInterface) {
        this.presenterInterface = presenterInterface;
    }


    public void request_login(final String username, final String password, final Context context, final Dialog dialog) {

        StringRequest stringRequest;
        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(context);

        dialog.show();

        stringRequest = new StringRequest(Request.Method.POST, URL.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    presenterInterface.Response(response);
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", convertPassMd5(password));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void request_detail(final Context context, final Dialog dialog, String id_data) {

        StringRequest stringRequest;
        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(context);

        dialog.show();

        stringRequest = new StringRequest(Request.Method.GET, "http://entry.sandbox.gits.id/api/alamku/index.php/api/get/detil/dataalam?itemid=" + id_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    presenterInterface.Response(response);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        requestQueue.add(stringRequest);
    }

    public void request_register(final String first_name, final String last_name, final String username, final String password, final String bdate, final String gender, final String phone, final Dialog dialog, Context context) {

        StringRequest stringRequest;
        RequestQueue requestQueue;

        dialog.show();

        requestQueue = Volley.newRequestQueue(context);

        stringRequest = new StringRequest(Request.Method.POST, URL.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    presenterInterface.Response(response);
                    dialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("username", username);
                params.put("password", password);
                params.put("bdate", bdate);
                params.put("gender", gender);
                params.put("phone", phone);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void request_data(Context context, Integer kategori, final Dialog dialog) {

        StringRequest stringRequest;
        RequestQueue requestQueue;

        dialog.show();

        requestQueue = Volley.newRequestQueue(context);

        stringRequest = new StringRequest(Request.Method.GET, URL.data_url + kategori, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    dialog.dismiss();
                    presenterInterface.Response(response);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        })
        ;

        requestQueue.add(stringRequest);
    }

    public void request_banner(Context context, Integer kategori) {

        StringRequest stringRequest;
        RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(context);

        stringRequest = new StringRequest(Request.Method.GET, URL.banner_url + kategori, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {

                    presenterInterface.ResponseBanner(response);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        })
        ;

        requestQueue.add(stringRequest);
    }

    private static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
