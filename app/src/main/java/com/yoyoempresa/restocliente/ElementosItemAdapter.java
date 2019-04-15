package com.yoyoempresa.restocliente;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ElementosItemAdapter extends BaseAdapter {
    static class  ElementosDatos {
        String nombre;
        String descripcion;
        String extras;
        Double precio;
        String url;
    }

    private Context context;
    private ArrayList<ElementosDatos> arrayelementos;

    ElementosItemAdapter(Context context, ArrayList<ElementosDatos> arrayelementos){
         this.context = context;
         this.arrayelementos = arrayelementos;
    }
    @Override
    public int getCount() {
        return arrayelementos.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayelementos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.listview_elementos_adapter,null);
        TextView tv_nombre = convertView.findViewById(R.id.tv_nombre_elemento);
        TextView tv_desc = convertView.findViewById(R.id.tv_elemento_desc);
        TextView tv_precio = convertView.findViewById(R.id.tv_elemento_precio);
        ImageView imagen = convertView.findViewById(R.id.imagen_elementos_lista);
        String descripcion = arrayelementos.get(position).descripcion;
        String URL = arrayelementos.get(position).url;
        tv_nombre.setText(arrayelementos.get(position).nombre);
        String precio = String.valueOf(arrayelementos.get(position).precio+ "$$");
        tv_precio.setText(precio);
        if (descripcion != null) {
            tv_desc.setText(descripcion);
        }

        if (URL != null) {
            Glide.with(convertView).load(URL).into(imagen);
        }
        return convertView;
    }

    public static class CartaAdapter extends BaseAdapter {
        ArrayList<DatosCartaAdapter> arraycarta;
        Context context;
        static class DatosCartaAdapter{
            String Nombre;
            String URL;
        }
        CartaAdapter(Context context, ArrayList<DatosCartaAdapter> arraycarta){
            this.context = context;
            this.arraycarta = arraycarta;
        }
        @Override
        public int getCount() {
            return arraycarta.size();
        }

        @Override
        public Object getItem(int position) {
            return arraycarta.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.listview_carta_adapter,null);
            TextView tv_carta = convertView.findViewById(R.id.tv_nombre_carta);
            String nombre = arraycarta.get(position).Nombre;
            tv_carta.setText(nombre);
            return convertView;
        }
    }
}
