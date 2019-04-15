package com.yoyoempresa.restocliente;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class Main2Activity extends FragmentActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Su posicion Actual"));
                    googleMap.setMinZoomPreference(15);
                    googleMap.setMinZoomPreference(20);
                    Adress.setAdress(getAdress(latLng));//crea una instancia de la ubicacion del usuario
                    EditText et = findViewById(R.id.editText6);
                    et.setText(Adress.getAdress());

                }

            }
        });
    }


    public void IniciarHome(View view) {
        EditText et = findViewById(R.id.editText6);
        et.setText(Adress.getAdress());
        if (et.length() > 0) {
            if (et.getText().toString().equals(Adress.getAdress())) {
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
            }else{
                Adress.setAdress(et.getText().toString());
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
            }

        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setMessage("La casilla de ubicacion no debe estar vacia")
                    .setPositiveButton("Aceptar", null).show();
        }
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }


    public String getAdress(LatLng latlang2) {

        //Obtiene la direccion de un par latitud, longitud
        String adress= null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            adress = geocoder.getFromLocation(latlang2.latitude, latlang2.longitude, 1)
                    .get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adress;
    }


}
