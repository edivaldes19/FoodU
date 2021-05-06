package Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.manuel.foodu.MainActivity;
import com.manuel.foodu.NegocioActivity;
import com.manuel.foodu.R;

import java.util.Locale;
import java.util.Objects;

import Mapa.LatitudLongitud;
import Mapa.Marcadores;

public class MapsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    View rootView;
    MapView mapView;
    GoogleMap gMap;
    Geocoder geocoder;
    MarkerOptions marker;
    Marcadores mark = new Marcadores();
    FloatingActionButton fab;
    SwitchMaterial switchMaterial;
    LocationManager locationManager;
    final int permiso = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        switchMaterial = rootView.findViewById(R.id.switchSatelite);
        switchMaterial.setChecked(false);
        switchMaterial.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            } else {
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchMaterial.setChecked(false);
        mapView = rootView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        solicitarPermiso();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        CheckInternetConnection();
        switchMaterial.setChecked(false);
        gMap = googleMap;
        gMap.setMinZoomPreference(13);
        gMap.setMaxZoomPreference(21);
        for (int i = 0; i < LatitudLongitud.Latitud.size(); i++) {
            double latitud = LatitudLongitud.Latitud.get(i);
            double longitud = LatitudLongitud.Longitud.get(i);
            String titulo = LatitudLongitud.Titulos.get(i);
            marker = mark.Marcador(latitud, longitud, titulo);
            gMap.addMarker(marker);
        }
        CameraPosition camera = new CameraPosition.Builder().target(mark.posicionDefecto()).zoom(13).bearing(0).tilt(0).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(mark.posicionDefecto()));
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        gMap.setOnMarkerClickListener(marker -> {
            Intent intent = new Intent(getActivity(), NegocioActivity.class);
            intent.putExtra("Titulo", marker.getTitle());
            Objects.requireNonNull(getActivity()).startActivity(intent);
            return true;
        });
        locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);
    }

    public void CheckInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Hay conexión a internet
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Error de red")
                    .setMessage("¿Desea ir a la configuración de conexión del dispositivo?")
                    .setPositiveButton("ACEPTAR", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        MapsFragment.this.startActivity(intent);
                    })
                    .setNegativeButton("CANCELAR", (dialog, which) -> {
                        Intent intent = new Intent(MapsFragment.this.getContext(), MainActivity.class);
                        MapsFragment.this.startActivity(intent);
                    })
                    .show();
        }
    }

    public boolean IsGPSEnabled() {
        try {
            int gpsSignal = Settings.Secure.getInt(Objects.requireNonNull(getActivity()).getContentResolver(), Settings.Secure.LOCATION_MODE);
            return gpsSignal != 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showInfoAlert() {
        new AlertDialog.Builder(getContext())
                .setTitle("Permiso de GPS")
                .setMessage("¿Desea ir a la configuración de ubicación del dispositivo?")
                .setPositiveButton("ACEPTAR", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MapsFragment.this.startActivity(intent);
                })
                .setNegativeButton("CANCELAR", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        if (!this.IsGPSEnabled()) {
            showInfoAlert();
        }
    }

    public void solicitarPermiso() {
        int permisoUbicacion = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permisoUbicacion != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permiso);
        }
    }
}