package com.manuel.foodu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.CategoryFragment;
import Fragments.MapsFragment;
import Mapa.Establecimiento;

public class MainActivity extends AppCompatActivity implements ComunicarActivitys {
    CategoryFragment categoryFragment = new CategoryFragment();
    MapsFragment mapsFragment = new MapsFragment();
    boolean isPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(categoryFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_acercade) {
            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_contactanos) {
            Intent intent = new Intent(this, ContactanosActivity.class);
            startActivity(intent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.firstFragment:
                loadFragment(categoryFragment);
                return true;
            case R.id.secondFragment:
                loadFragment(mapsFragment);
                return true;
        }
        return false;
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void enviarEstablecimiento(Establecimiento establecimiento) {
        Intent intent = new Intent(this, NegocioActivity.class);
        intent.putExtra("Titulo", establecimiento.getNombre());
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isPressed) {
            finishAffinity();
            System.exit(0);
        } else {
            Toast.makeText(getApplicationContext(), "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
            isPressed = true;
        }
        Runnable runnable = () -> isPressed = false;
        new Handler().postDelayed(runnable, 2000);
    }
}