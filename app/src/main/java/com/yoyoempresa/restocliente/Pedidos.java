package com.yoyoempresa.restocliente;




public class Pedidos {

    private String cliente;
    private String nombre;
    private double precio;
    private String  adress;
    private  String status;

    public Pedidos() {

    }

    Pedidos(String cliente, String nombre, Double precio, String adress, String status){
        this.cliente = cliente;
        this.nombre = nombre;
        this.precio = precio;
        this.adress = adress;
        this.status = status;
    }



    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getAdress() {
        return adress;
    }

    public String getStatus() {
        return status;
    }
}
