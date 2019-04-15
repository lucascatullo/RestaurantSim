package com.yoyoempresa.restocliente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent i = new Intent(this, elementos_carta.NotificacionesReal.class);
        startService(i);
    }

    public void opiniones(View v) {
        Intent i = new Intent(this, opiniones.class);
        startActivity(i);
    }

    public void notificaciones(View view) {
        Intent i = new Intent(this, notificaciones.class);
        startActivity(i);
    }

    public void  cupones(View view) {
        Intent i = new Intent(this, cupones.class);
        startActivity(i);
    }

    public boolean haycompras() {
        ArrayList<elementos_carta.ComprasAdapter.Compras> arraycompras;
        arraycompras = elementos_carta.ComprasAdapter.ComprasArray.getComprasArray();
        return arraycompras.size() > 0;
    }

    public void Confirmarcompra(View v) {
        if (haycompras()) {
            Intent i = new Intent(this, ConfirmarCompras.class);
            startActivity(i);
        }else{
            Toast.makeText(this,"El carrito esta vacio",Toast.LENGTH_SHORT).show();
        }
    }

    public void menu(View v) {
        Intent i = new Intent(this, menu.class);
        startActivity(i);
    }
}
