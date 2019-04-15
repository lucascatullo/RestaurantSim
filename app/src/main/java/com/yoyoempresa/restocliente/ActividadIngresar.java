package com.yoyoempresa.restocliente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

import static com.google.android.gms.auth.api.credentials.CredentialPickerConfig.Prompt.SIGN_IN;

public class ActividadIngresar extends AppCompatActivity {
    boolean a;
    private FirebaseAuth Mauth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_ingresar);
        Mauth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.INVISIBLE);
    }


    public void ingresarUserPasswd(View view) {
        progressBar.setVisibility(View.VISIBLE);
        EditText et_email = findViewById(R.id.et_usuario_ingresar);
        EditText et_passwd = findViewById(R.id.et_contraseña_ingresar);
        if (et_email.length()> 0 && et_passwd.length()>0) {
            String email = et_email.getText().toString();
            String passwd= et_passwd.getText().toString();
            Mauth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (!task.isSuccessful()) {
                        Toast.makeText(ActividadIngresar.this,task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }else{
                        Intent i = new Intent(ActividadIngresar.this,Main2Activity.class);
                        startActivity(i);
                    }
                }
            });
        }
    }

    public void ingresarGoogle(View view) {
        List<AuthUI.IdpConfig> provedor = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(provedor)
                        .build(), SIGN_IN);
    }



    public void onActivityResult(int RequestCode, int resultCode, Intent data) {
        if (RequestCode == SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                Adress.setCurrentUser();
                a = true;
                Intent i = new Intent(this, Main2Activity.class);
                startActivity(i);
            }else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActividadIngresar.this);
                builder.setMessage("Imposible conectar con el servidor, intentelo mas tarde")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        }
    }

}
