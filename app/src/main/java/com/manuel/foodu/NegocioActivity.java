package com.manuel.foodu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import Mapa.Establecimiento;
import Mapa.LatitudLongitud;

public class NegocioActivity extends AppCompatActivity implements ToolTipsManager.TipListener, View.OnClickListener {
    CoordinatorLayout coordinatorLayout;
    ImageView imagen, llamar, mensaje, whatsapp;
    TextView titulo, tipo, direccion, sitioWeb, horario, servicio, telefono;
    ToolTipsManager toolTipsManager;
    String nombre, numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio);
        coordinatorLayout = findViewById(R.id.cordinator);
        imagen = findViewById(R.id.imageView_establecimiento);
        llamar = findViewById(R.id.iv_telefono);
        mensaje = findViewById(R.id.iv_mensaje);
        whatsapp = findViewById(R.id.iv_whatsapp);
        titulo = findViewById(R.id.textView_Nombre);
        tipo = findViewById(R.id.textView_Tipo);
        direccion = findViewById(R.id.textView_Direccion);
        sitioWeb = findViewById(R.id.textView_Web);
        horario = findViewById(R.id.textView_Horario);
        servicio = findViewById(R.id.textView_Servicio);
        telefono = findViewById(R.id.textView_Telefono);
        toolTipsManager = new ToolTipsManager(this);
        String s = (String) getIntent().getExtras().get("Titulo");
        setTitle(s);
        Establecimiento establecimiento = getID(s);
        imagen.setImageResource(establecimiento.getImagen());
        titulo.setText(establecimiento.getNombre());
        tipo.setText(establecimiento.getTipo());
        direccion.setText(establecimiento.getDireccion());
        sitioWeb.setText(establecimiento.getSitioWeb());
        horario.setText(establecimiento.getHorario());
        servicio.setText(establecimiento.isServicio() ? "Contamos con Servicio a Domicilio" : "No Contamos con Servicio a Domicilio");
        telefono.setText(establecimiento.getTelefono());
        String s1 = titulo.getText().toString();
        String string1 = telefono.getText().toString().trim();
        String string2 = string1.replaceAll(" ", "");
        nombre = s1;
        numero = string2;
        mostrarUnaVez();
        solicitarPermisos();
        llamar.setOnClickListener(this);
        mensaje.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
    }

    public Establecimiento getID(String titulo) {
        Establecimiento o = null;
        for (int i = 0; i < LatitudLongitud.Latitud.size(); i++) {
            if (LatitudLongitud.Titulos.get(i).equals(titulo)) {
                o = LatitudLongitud.establecimiento.get(i);
                break;
            }
        }
        return o;
    }

    public boolean solicitarPermisos() {
        int permisoLlamadas = ActivityCompat.checkSelfPermission(NegocioActivity.this, Manifest.permission.CALL_PHONE);
        int permisoMensajes = ActivityCompat.checkSelfPermission(NegocioActivity.this, Manifest.permission.SEND_SMS);
        if (permisoLlamadas != PackageManager.PERMISSION_GRANTED || permisoMensajes != PackageManager.PERMISSION_GRANTED) {
            int permiso = 0;
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, permiso);
            return false;
        }
        return true;
    }

    public void mostrarUnaVez() {
        SharedPreferences sp = getSharedPreferences("MY_APP_FLAGS", 0);
        boolean firstRun = sp.getBoolean("first_run", true);
        if (firstRun) {
            Snackbar.make(coordinatorLayout, "Para contactar al establecimiento oprima los iconos inferiores", Snackbar.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("first_run", false).apply();
        }
    }

    public void mostrarTip(String mensaje) {
        int posicion = ToolTip.POSITION_ABOVE;
        int align = ToolTip.ALIGN_RIGHT;
        toolTipsManager.findAndDismiss(llamar);
        ToolTip.Builder builder = new ToolTip.Builder(this, telefono, coordinatorLayout, mensaje, posicion);
        builder.setAlign(align);
        builder.setBackgroundColor(Color.parseColor("#FF6600"));
        toolTipsManager.show(builder.build());
    }

    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
    }

    @SuppressLint({"NonConstantResourceId", "QueryPermissionsNeeded"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_telefono:
                toolTipsManager.dismissAll();
                if (solicitarPermisos()) {
                    if (numero.equals("TeléfonoNoDisponible")) {
                        mostrarTip(nombre + " no cuenta con número de teléfono para llamar");
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + numero));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                } else {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    Toast.makeText(getApplicationContext(), "El permiso de Teléfono para FoodU esta desactivado, actívelo manualmente", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_mensaje:
                toolTipsManager.dismissAll();
                if (solicitarPermisos()) {
                    if (numero.equals("TeléfonoNoDisponible")) {
                        mostrarTip(nombre + " no cuenta con número de teléfono para enviar mensajes");
                    } else {
                        Uri uri = Uri.parse("smsto:" + numero);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        startActivity(intent);
                    }
                } else {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    Toast.makeText(getApplicationContext(), "El permiso de SMS para FoodU esta desactivado, actívelo manualmente", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_whatsapp:
                toolTipsManager.dismissAll();
                if (numero.equals("TeléfonoNoDisponible")) {
                    mostrarTip(nombre + " no cuenta con número de teléfono para enviar WhatsApp");
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    String uri = "whatsapp://send?phone=52" + numero;
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
                break;
        }
    }
}