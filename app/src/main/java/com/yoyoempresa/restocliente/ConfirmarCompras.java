package com.yoyoempresa.restocliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfirmarCompras extends AppCompatActivity {

    ArrayList<elementos_carta.ComprasAdapter.Compras> comprasarray;
    TextView total ;
    ListView lv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_compras);
        total = findViewById(R.id.textView16);
        comprasarray = elementos_carta.ComprasAdapter.ComprasArray.getComprasArray();
        String preciototal = "Total: " + String.valueOf(total_pagar()+ "$");
        total.setText(preciototal);
        lv = findViewById(R.id.lv_compras);
        et = findViewById(R.id.editText7);
        mostrarcompras();
        ListaClick();
    }

    public void subircomentario() {
        if (et.length()>0) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            Map<String, String> comentario = new HashMap<>();
            comentario.put("user", Objects.requireNonNull(user.getDisplayName()));
            comentario.put("comentario", et.getText().toString());
            db.collection("Comentarios").add(comentario);
        }
    }


    public void mostrarcompras() {
        // carga todas las cosas compradas en LV
        elementos_carta.ComprasAdapter adapter = new elementos_carta.ComprasAdapter(this, comprasarray);
        lv.setAdapter(adapter);
    }


    public void ListaClick() {
        //pone click listener de la compra para eliminar
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // elimina el elemento seleccionado
                dialogo_eliminar(position);
                ActualizarPrecio();
            }
        });
    }


    public void ActualizarPrecio() {
        // se ejecuta luego de eliminar un objeto para actualizar el precio
        String preciototal = "Total: " + String.valueOf(total_pagar()+"$");
        total.setText(preciototal);
    }


    public void dialogo_eliminar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarCompras.this);
        builder.setMessage("Se eliminar el item: \n"+ comprasarray.get(position).nombre)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        comprasarray.remove(position);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    public void dialogo_confirmar_compra(View v) {
        if (comprasarray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarCompras.this);
            builder.setMessage("El monto a pagar es:\n " + String.valueOf(total_pagar()))
                    .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // sube el pedido a la base de datos para ser Confirmado.
                            SubirPedido();
                            subircomentario();
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }
    }




    public void SubirPedido() {
        //Una vez confirmado el pedido , carga los datos a firebase para ser confirmados por el resto
        Pedidos pedidos =
                new Pedidos(Adress.getCurrentUser(), TodosLosNombresPedidos(),total_pagar(),Adress.getAdress(),"En PROCESO");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Pedidos").document(Adress.getCurrentUser()).set(pedidos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ConfirmarCompras.this, "Pedido Realizado", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

    }

    public String TodosLosNombresPedidos() {
        String todo = " ";
        elementos_carta.ComprasAdapter.Compras compras;
        for (int i = 0; i < comprasarray.size(); i++) {
            compras = comprasarray.get(i);
            todo = todo + compras.cantidad +" "+  compras.nombre+ " "+compras.extras+ " ";
        }
        return todo;
    }


    public double total_pagar() {
        // Calcula lo que tiene que pagar el usuario
        double total = 0;
        for (int i = 0; i < comprasarray.size(); i++) {
            total = total + comprasarray.get(i).precio * comprasarray.get(i).cantidad;
        }
        return total;
    }


    public void volver() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }


}
