package com.yoyoempresa.restocliente;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class opiniones extends AppCompatActivity {


    ArrayList<cupones.ComentariosAdapter.Comentario> arrayComentario = new ArrayList<>();
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opiniones);
        lv = findViewById(R.id.lv_comentarios);
        cargarArray();
        oculta();
    }




    public void mostrarListaComentarios() {
        cupones.ComentariosAdapter adapter = new cupones.ComentariosAdapter(opiniones.this, arrayComentario);
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    public void arrayvacio() {
        if (arrayComentario.size() > 0) {
            oculta();
            mostrarListaComentarios();
        }else{
            lv.setVisibility(View.INVISIBLE);
        }
    }

    public void cargarArray() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Comentarios").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayComentario.clear();
                            cupones.ComentariosAdapter.Comentario comentario = new cupones.ComentariosAdapter.Comentario();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                comentario.usuario = (String) document.get("user");
                                comentario.Comentario = (String) document.get("comentario");
                                arrayComentario.add(comentario);
                            }
                            arrayvacio();
                        }
                    }
                });

    }

    public void oculta() {
        TextView tv1,tv2,tv3;
        ImageView imageView;
        tv1 = findViewById(R.id.textopiniones);
        tv2 = findViewById(R.id.textfondo);
        tv3 = findViewById(R.id.text_opínion);
        imageView = findViewById(R.id.imagendialogo);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
    }
}
