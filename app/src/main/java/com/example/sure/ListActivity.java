package com.example.sure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE ="com.example.sure.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    public void openInfoX(View v){
        Intent intent = new Intent(this, InfoActivity.class);

        ImageButton imageButton =  findViewById(R.id.imageButton4);
        String message = imageButton.getContentDescription().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }
}