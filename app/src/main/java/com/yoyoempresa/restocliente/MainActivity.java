package com.yoyoempresa.restocliente;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;


public class MainActivity extends AppCompatActivity {

    private PaymentsClient paymentsClient;
    int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paymentsClient = Wallet.getPaymentsClient(this,new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build());
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }





    public void ingresar(View view) {
        // se ejecuta cuando inician sesion
        Intent i = new Intent(this, ActividadIngresar.class);
        startActivity(i);
    }

    public void ingresarnuevo(View view) {
        // se ejecuta cuando inicias sesion por primera vez //
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void mostrarboton(final Double precio){
        final Optional<JSONObject> isReadyToPayRequest = GooglePay.getIsReadyToPayRequest();
            if (!isReadyToPayRequest.isPresent()) {
                return;
            }
        IsReadyToPayRequest request;
            request = IsReadyToPayRequest.fromJson(isReadyToPayRequest.get()
                    .toString());
        if (request == null) {
            return;
        }
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                try{
                    boolean result = task.getResult(ApiException.class);
                    if (result) {
                        try {
                            requestPayment(precio);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(Double precio) throws JSONException {
        Optional<JSONObject> paymentDataRequestJson = GooglePay.getPaymentDataRequest(precio);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!paymentDataRequestJson.isPresent()) {
                return;
            }
        }
        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
        if (request != null) {
            AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),this
                    ,LOAD_PAYMENT_DATA_REQUEST_CODE);
        }

    }

    public void onActivityResult(int RequestCode, int resultCode, Intent data) {
        if (RequestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentData paymentData = PaymentData.getFromIntent(data);
                String json = paymentData.toJson();
                JSONObject paymentMethodData = null;
                try {
                    paymentMethodData = new JSONObject(json)
                            .getJSONObject("paymentMethodData");
                    String paymentToken = paymentMethodData
                            .getJSONObject("tokenizationData")
                            .getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
