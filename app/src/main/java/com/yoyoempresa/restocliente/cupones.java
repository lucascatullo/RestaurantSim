package com.yoyoempresa.restocliente;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class cupones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupones);
    }

    public static class ComentariosAdapter extends BaseAdapter {


        private ArrayList<Comentario> arrayComentario;
        private Context context;



        ComentariosAdapter(Context context, ArrayList<Comentario> arrayComentario) {
            this.context = context;
            this.arrayComentario = arrayComentario;
        }

        @Override
        public int getCount() {
            return arrayComentario.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayComentario.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_comentarios,null);
            TextView usuario = convertView.findViewById(R.id.tv_usuario);
            TextView comentario = convertView.findViewById(R.id.tv_comentario);
            usuario.setText(arrayComentario.get(position).usuario);
            comentario.setText(arrayComentario.get(position).Comentario);
            return convertView;
        }

        static class Comentario {
            String usuario;
            String Comentario;
        }

    }
}
