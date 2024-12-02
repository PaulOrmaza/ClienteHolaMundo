package com.spoj.clientehola;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView txtResponse;
    private Button btnSendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos de la interfaz
        txtResponse = findViewById(R.id.txtResponse);
        btnSendRequest = findViewById(R.id.btnSendRequest);

        // Evento para el botón
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchServerResponse(); // Llama a la función para obtener la respuesta del servidor
            }
        });
    }

    // Función para obtener la respuesta del servidor
    private void fetchServerResponse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // URL del servidor (reemplázala con la URL de tu servidor)
                    URL url = new URL("http://192.168.137.79:3000");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Lee la respuesta
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Actualiza el TextView en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtResponse.setText(response.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();

                    // Muestra un mensaje de error en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}
