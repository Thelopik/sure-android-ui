package com.example.sure;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        ImageView imageView = findViewById(R.id.imgView);
        Picasso.with(this).load(extras.getString("IMG")).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAdjustViewBounds(true);

        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(extras.getString("NAME"));
        TextView textViewText = findViewById(R.id.textViewText);
        textViewText.setText(extras.getString("TEXT"));
        TextView textViewLat = findViewById(R.id.textViewLat);
        textViewLat.setText(extras.getString("LAT"));
        TextView textViewLon = findViewById(R.id.textViewLon);
        textViewLon.setText(extras.getString("LON"));
    }
}