package com.yoyoempresa.restocliente;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class elementos_carta extends AppCompatActivity {
    String source;
    ArrayList<ElementosItemAdapter.ElementosDatos> arrayelementos= new ArrayList<>();
    ListView lv_elementos;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elementos_carta);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        Bundle vin = getIntent().getExtras();
        source = (String) Objects.requireNonNull(vin).get("source");
        lv_elementos = findViewById(R.id.lv_elemento);
        mostrar_elementos();


    }

    public void mostrar_elementos() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Carta").document(source).collection("Items")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arrayelementos.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        ElementosItemAdapter.ElementosDatos datos = new ElementosItemAdapter.ElementosDatos();
                        datos.nombre = (String) document.get("nombre");
                        datos.descripcion = (String) document.get("descripcion");
                        datos.extras = (String) document.get("extras");
                        datos.precio = ((Long) Objects.requireNonNull(document.get("precio")))
                                .doubleValue();
                        datos.url = (String)document.get("url");
                        arrayelementos.add(datos);
                    }
                    ElementosItemAdapter adapter =
                            new ElementosItemAdapter(elementos_carta.this, arrayelementos);
                    lv_elementos.setAdapter(adapter);
                    AdapterView.OnItemClickListener onItemClickListener = listaClick();
                    lv_elementos.setOnItemClickListener(onItemClickListener);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public AdapterView.OnItemClickListener listaClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String nombre = arrayelementos.get(position).nombre;
                    Double precio = arrayelementos.get(position).precio;
                    Dialog_compras(nombre,precio);
                }

        };
    }

    public void AddCarrito(String nombre,@Nullable String extras, int Cantidad,double precio) {
        ComprasAdapter.Compras compras= new ComprasAdapter.Compras();
        compras.nombre = nombre;
        compras.cantidad = Cantidad;
        compras.precio = precio;
        if (extras != null) {
            compras.extras = extras;
        }
        compras.CargaArray(compras);
        Toast.makeText(this, "Compra realizada", Toast.LENGTH_SHORT).show();
    }

    public void Dialog_compras(final String nombre,final double precio) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(elementos_carta.this);
        LayoutInflater li = getLayoutInflater();
        final View view = li.inflate(R.layout.dialogo_compras, null);
        final EditText et_cantidad = view.findViewById(R.id.et_compras_cantidad);
        final EditText et_extras = view.findViewById(R.id.et_compras_extras);
        builder.setView(view).setPositiveButton("Comprar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (et_cantidad.length()>0){
                final int cantidad = Integer.parseInt(et_cantidad.getText().toString());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(elementos_carta.this);
                builder1.setMessage("Desea Confirmar su compra?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AddCarrito(nombre,et_extras.getText().toString(),cantidad,precio);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
                } else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(elementos_carta.this);
                builder1.setMessage("Usted debe ingresar la cantidad que desee comprar")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
            }

        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }


    public static class ComprasAdapter extends BaseAdapter {

        private Context context;
        private static ArrayList<Compras> comprasArray=new ArrayList<>();


         ComprasAdapter(Context context, ArrayList<Compras> comprasArray) {
            this.context = context;
            ComprasAdapter.comprasArray = comprasArray;
        }

        static class Compras{
            String nombre;
            double precio;
            int cantidad;
            String extras;


            void CargaArray(Compras compras) {
                comprasArray.add(compras);
            }

        }



        static class ComprasArray{
             static ArrayList<Compras> getComprasArray() {
                    return comprasArray;
            }
        }



        @Override
        public int getCount() {
            return comprasArray.size();
        }

        @Override
        public Object getItem(int position) {
            return comprasArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.lista_confirmar_compras, null);
            TextView nombre = convertView.findViewById(R.id.tv_confirmar_compra_nombre);
            TextView cantidad = convertView.findViewById(R.id.tv_compra_confirmacion_cantidad);
            TextView precio = convertView.findViewById(R.id.tv_compra_confirmacion_precio);
            nombre.setText(comprasArray.get(position).nombre);
            String cadenaCantidad = String.valueOf(comprasArray.get(position).cantidad);
            cantidad.setText(cadenaCantidad);
            String cadenaPrecio = String.valueOf(comprasArray.get(position).precio);
            precio.setText(cadenaPrecio);
            return convertView;
        }
    }

    public static class NotificacionesReal extends Service {


        Context context;
        FirebaseAuth Mauth;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            return START_STICKY;
        }

        public void onCreate() {
            Mauth = FirebaseAuth.getInstance();
            context = getBaseContext();
            ObtenerDatosUSuario();
        }


        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }



        public void ObtenerDatosUSuario() {
            // tambien envia notificacion si el resto acepto el pedido
                FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = db.collection("Pedidos")
                    .document(Objects.requireNonNull(Objects.requireNonNull(Mauth.getCurrentUser()).getDisplayName()));
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Error", "Se produjo un error inesperado", e);
                            return;
                        }
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            String status  = (String) documentSnapshot.get("status");
                            if (status!=null && status.equals("ACEPTADO")) {
                                createNotificacion();
                            }
                        }
                    }
                });


            }


            public void createNotificacion() {
                Intent i = new Intent();
                i.putExtra("idScreen", 12);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context , "a");
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder.setContentTitle("Pedido")
                        .setContentText("Su pedido a sido aceptado")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());

            }
        }
}
