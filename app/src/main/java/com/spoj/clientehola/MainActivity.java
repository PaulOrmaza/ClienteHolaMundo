package com.spoj.clientehola;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private TextView txt_respuesta;
    private Button btn_enviar_solicitud;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_respuesta = findViewById(R.id.txt_respuesta);
        btn_enviar_solicitud = findViewById(R.id.btn_enviar_solicitud);


        btn_enviar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitud();
            }
        });
    }


    private void enviarSolicitud() {

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor
                    URL url = new URL("http://192.168.137.79:3003/nombre");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");


                    // 2. Leer la respuesta del servidor
                    // BufferedReader para leer la respuesta línea por línea
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder respuesta = new StringBuilder(); // Almacena toda la respuesta del servidor
                    String linea;

                    // Mientras haya contenido, añadirlo al StringBuilder
                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);

                    }
                    reader.close(); // Cerramos el lector después de leer


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Convertir la respuesta en un JSONArray
                                JSONArray jsonArray = new JSONArray(respuesta.toString());

                                // Construir un texto con los valores del JSONArray
                                StringBuilder texto = new StringBuilder();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    // Obtener cada objeto del JSON
                                    texto.append(" ").append(i + 1).append(": ").append(jsonArray.get(i)).append("\n");
                                }

                                // Mostrar los datos procesados en el TextView
                                txt_respuesta.setText(texto.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error al procesar el JSON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir el error en consola para depuración


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start();
    }
}