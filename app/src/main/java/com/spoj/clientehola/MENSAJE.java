package com.spoj.clientehola;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MENSAJE extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private TextView txt_respuesta_mensaje;
    private Button btn_solicitar_mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje);

        txt_respuesta_mensaje = findViewById(R.id.txt_respuesta_mensaje);
        btn_solicitar_mensaje = findViewById(R.id.btn_solicitar_mensaje);

        // Configurar el evento del botón
        btn_solicitar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarMensaje();
            }
        });
    }

    private void solicitarMensaje() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Establecer conexión con el servidor
                    URL url = new URL("http://192.168.137.79:3003/mensaje"); // Endpoint para mensajes
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 2. Leer la respuesta del servidor
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder respuesta = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }
                    reader.close();

                    // 3. Procesar la respuesta en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Procesar la respuesta como un JSONObject
                                JSONObject jsonObject = new JSONObject(respuesta.toString());

                                // Obtener datos específicos del mensaje
                                String mensaje = jsonObject.getString("mensaje");


                                // Formatear y mostrar el mensaje
                                String textoFormateado = "Mensaje: " + mensaje;
                                txt_respuesta_mensaje.setText(textoFormateado);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MENSAJE.this, "Error al procesar el JSON", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MENSAJE.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start();
    }
}
