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

    public void camera(View view){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }

    public void gallery(View view){
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

/*
Photos inspiré de : https://stackoverflow.com/questions/13977245/android-open-camera-from-button
Objectif prochaine version photo : https://www.youtube.com/watch?v=kanbIK-Jf3A
Gallerie inspiré de : https://stackoverflow.com/questions/16928727/open-gallery-app-from-android-intent/23821227
*/
