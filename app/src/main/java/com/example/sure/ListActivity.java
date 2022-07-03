package com.example.sure;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

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

    public void openMap(View v){
        Intent i = new Intent(this, OsmActivity.class);
        startActivity(i);
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
                imageButton.setContentDescription(p.getName());
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