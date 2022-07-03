package com.example.sure;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RequestHandler {
    public static void getPlaces(Context context, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://10.0.2.2:8080/places";//emulator nutzt virtual router zum dev device

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RequestHandler Response", response);
                        try {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<ArrayList<Place>>() {}.getType();
                            ArrayList<Place> placeArrayList = new Gson().fromJson(response , listType);
                            callback.onSuccess(placeArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RequestHandler Error ", error.toString());
            }
        });
        queue.add(stringRequest);
    }

    public static void getPlaceById(Context context, String id, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://10.0.2.2:8080/places/"+ id;//emulator nutzt virtual router zum dev device

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RequestHandler Response", response);
                        try {
                            Gson gson = new Gson();
                            Place place = new Gson().fromJson(response , Place.class);
                            callback.onSuccess(place);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RequestHandler Error ", error.toString());
            }
        });
        queue.add(stringRequest);
    }

}
