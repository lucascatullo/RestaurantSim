package com.yoyoempresa.restocliente;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends FragmentActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    private FirebaseAuth Mauth;
    ProgressBar progressBar;


    public boolean PasswdCoinciden(String passwd1 , String passwd2) {
        return passwd1.equals(passwd2);
    }

    public boolean Contratrim(String passwd) {
        //verifica que la contraseña no tenga espacios adelante
        return passwd.trim().equals(passwd);
    }

    public boolean PasswdMasdeseis(String passwd) {
        // veridifa que la contraseña tenga la menos 6 digitos
        return passwd.toCharArray().length >= 6;
    }


    public void RegristrarNuevoUsuario(View view) {

        // verifica las condiciones de validacion , (Ej, passwd iguales cuando se presiona el boton
        progressBar.setVisibility(View.VISIBLE);
        EditText etpass1 = findViewById(R.id.editText4);
        EditText etpass2 = findViewById(R.id.editText5);
        EditText etemail = findViewById(R.id.editText3);
        String email = etemail.getText().toString();
        String passwd1 = etpass1.getText().toString();
        String passwd2 = etpass2.getText().toString();
        if (Contratrim(passwd1)) {
            if (PasswdMasdeseis(passwd1)) {
                if (PasswdCoinciden(passwd1, passwd2)) {
                    RegistroenFirebase(email, passwd1);
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden",
                            Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Registro.this,
                        "La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(Registro.this,
                    "Las contraseñas no pueden empezar con espacio",Toast.LENGTH_SHORT).show();
            }
    }

    public void RegistroenFirebase(String email,String passwd) {

        // envia los datos al servidor de database para ser registrados
        Mauth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (!task.isSuccessful()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                    builder.setMessage("Error al crear usuario. Intentelo de nuevo mas tarde")
                            .setPositiveButton("Aceptar", null)
                            .create().show();
                }else{
                    Toast.makeText(Registro.this, "Usuarío creado correctamente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_inicio_sesion);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Mauth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
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
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions()
                            .position(latLng).title("Tu posición actual: "));
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.setMaxZoomPreference(20);
                    map.setMinZoomPreference(15);
                }

            }
        });
    }
}
