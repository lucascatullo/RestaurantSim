package com.yoyoempresa.restocliente;

import com.google.firebase.auth.FirebaseAuth;

class Adress {


    private static String currentUser;
    private static String direccion;


    Adress() {

    }

    static void setCurrentUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Adress.currentUser = firebaseAuth.getCurrentUser().getDisplayName();
        }
    }

    static String getCurrentUser() {
        return currentUser;
    }

    static void setAdress(String direccion) {
        Adress.direccion = direccion;
    }

    static String getAdress() {
        return direccion;
    }

}
