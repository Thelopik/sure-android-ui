package com.example.sure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = "woraround msg";//intent.getStringExtra(OsmActivity.EXTRA_MESSAGE);

        TextView textview = findViewById(R.id.textView);
        textview.setText(message);
    }
}