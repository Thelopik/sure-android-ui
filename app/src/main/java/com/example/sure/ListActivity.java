package com.example.sure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ArrayList<Place> globalPlaceArrayList;

    public static final String EXTRA_MESSAGE ="com.example.sure.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getPlaces(new VolleyCallback(){
            @Override
            public void onSuccess(ArrayList<Place> result) {
                globalPlaceArrayList = result;

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout2);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                for (Place p : globalPlaceArrayList) {
                    createListelem(linearLayout, params2, p.getId(), p.getImg());
                }

            }
            @Override
            public void onSuccess(Place place) {
            }
        });
    }

    public void openInfoX(View v){
        Intent intent = new Intent(this, InfoActivity.class);
        String message = v.getContentDescription().toString();

        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void createListelem(LinearLayout linearLayout, ViewGroup.LayoutParams params, int i, String img){

            System.out.println(i);
            ImageButton imageButton = new ImageButton(this);
            imageButton.setId(i);
            final int imgId_ = imageButton.getId();
            try{
                //Uri uri = new Uri(img);
                //imageButton.setImageURI(uri);
            } catch(Exception e){}



        imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
            imageButton.setAdjustViewBounds(true);
            linearLayout.addView(imageButton, params);
            System.out.println("button " + i + " erzeugt");
            imageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),
                                    "Button clicked index = " + imgId_, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }


    private void getPlaces(final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(ListActivity.this);
        String url = "http://10.0.2.2:8080/places";//emulator nutzt virtual router zum dev device

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response ", response);
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
                Log.d("Error ", error.toString());
            }
        });
        queue.add(stringRequest);
        Log.d("String Response ", stringRequest.toString());
    }
}