package com.yoyoempresa.restocliente;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class menu extends AppCompatActivity {
    String source;
    ProgressBar progressBar;
    ListView lv ;
    ArrayList<ElementosItemAdapter.CartaAdapter.DatosCartaAdapter> arraycarta = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        lv = findViewById(R.id.lv_carta);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mostrar();
    }



    public void List_view() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                source = arraycarta.get(position).Nombre;
                Intent i = new Intent(menu.this, elementos_carta.class);
                i.putExtra("source", source);
                startActivity(i);

            }
        });
    }



    public void mostrar() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Carta").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arraycarta.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        ElementosItemAdapter.CartaAdapter.DatosCartaAdapter datos = new ElementosItemAdapter.CartaAdapter.DatosCartaAdapter();
                        datos.Nombre = (String) document.get("Nombre");
                        datos.URL = (String) document.get("URL");
                        arraycarta.add(datos);
                    }
                    ElementosItemAdapter.CartaAdapter adapter = new ElementosItemAdapter.CartaAdapter(menu.this, arraycarta);
                    lv.setAdapter(adapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    List_view();
                }
            }
        });
    }
}
