package com.xpl.frameanim;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView=findViewById(R.id.iv);
        imageView.setBackgroundResource(R.drawable.girl_anim);
        AnimationDrawable anim= (AnimationDrawable) imageView.getBackground();
        anim.start();
    }
}
