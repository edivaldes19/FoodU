package com.manuel.foodu;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    ImageView imageView_restaurant;
    TextView textViewFoodU;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView_restaurant = findViewById(R.id.iv_restaurante);
        textViewFoodU = findViewById(R.id.text_viewNombre);
        SharedPreferences sp = getSharedPreferences("MY_APP_FLAGS", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first_run", true).apply();
        Objects.requireNonNull(getSupportActionBar()).hide();
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView_restaurant, PropertyValuesHolder.ofFloat("scaleX", 1.125f), PropertyValuesHolder.ofFloat("scaleY", 1.125f));
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
        animarTexto("FoodU");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }, 2500);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textViewFoodU.setText(charSequence.subSequence(0, index++));
            if (index <= charSequence.length()) {
                handler.postDelayed(runnable, delay);
            }
        }
    };

    public void animarTexto(CharSequence charSequence1) {
        charSequence = charSequence1;
        index = 0;
        textViewFoodU.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }
}