package com.example.andro.ecommerce.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.andro.ecommerce.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    /*    WindowManager.LayoutParams lp =this.getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(this.getWindow().getAttributes());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = (ImageView) findViewById(R.id.ecommerce_icon);
        imageView.setAlpha(0f);
        imageView.animate().alpha(1f).setDuration(2000).setInterpolator(new LinearInterpolator());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}
