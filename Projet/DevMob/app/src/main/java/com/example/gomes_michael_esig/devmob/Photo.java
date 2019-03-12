package com.example.gomes_michael_esig.devmob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Photo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
    }

    public void backMenu(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void camera(View view){
        //Inspiré de : https://stackoverflow.com/questions/13977245/android-open-camera-from-button
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    public void gallery(View view){
        //Inspiré de : https://stackoverflow.com/questions/16928727/open-gallery-app-from-android-intent/23821227
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
