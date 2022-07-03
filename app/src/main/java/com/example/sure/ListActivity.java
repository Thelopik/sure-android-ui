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
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ArrayList<Place> globalPlaceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        RequestHandler.getPlaces(this, new VolleyCallback(){
            @Override
            public void onSuccess(ArrayList<Place> result) {
                createListElements(result);
            }
            @Override
            public void onSuccess(Place place) {
            }
        });
    }

    public void openInfoX(Place p, View v){
        Intent intent = new Intent(this, InfoActivity.class);
        int id = v.getId();

        intent.putExtra("ID", String.valueOf(p.getId()));
        intent.putExtra("NAME", p.getName());
        intent.putExtra("IMG", p.getImg());
        intent.putExtra("TEXT", p.getText());
        intent.putExtra("LAT", String.valueOf(p.getLat()));
        intent.putExtra("LON", String.valueOf(p.getLon()));

        startActivity(intent);
    }

    public void createListElements(ArrayList<Place> result) {
        globalPlaceArrayList = result;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        for (Place p : globalPlaceArrayList) {
            ImageButton imageButton = new ImageButton(this);
            imageButton.setId(p.getId());
            final int imgId_ = imageButton.getId();
            try{
                Picasso.with(this).load(p.getImg()).into(imageButton);
                imageButton.setScaleType(ImageView.ScaleType.FIT_XY);
                imageButton.setAdjustViewBounds(true);
                linearLayout.addView(imageButton, params);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.d("BUTTON CLICKED", "button " + p.getId() + " clicked");
                        openInfoX(p, view);
                    }
                });
            } catch(Exception e){}

            Log.d("BUTTON CREATED", "button mit Place ID " + p.getId() + " erzeugt");
        }
    }

}