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

public class MENSAJE extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private TextView txt_respuesta_mensaje; // Para mostrar el mensaje del servidor
    private Button btn_solicitar_mensaje;   // Botón para solicitar el mensaje

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje); // Layout correspondiente a esta actividad

        // Vincular las referencias con los elementos del layout
        txt_respuesta_mensaje = findViewById(R.id.txt_respuesta_mensaje);
        btn_solicitar_mensaje = findViewById(R.id.btn_solicitar_mensaje);

        // Configuración del botón para realizar la solicitud
        btn_solicitar_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarMensaje(); // Llamada al método que realiza la solicitud
            }
        });
    }

    // Método que realiza la solicitud al servidor
    private void solicitarMensaje() {
        // Crear un hilo para realizar la solicitud en segundo plano
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor
                    URL url = new URL("http://192.168.137.79:3003/paul"); // Endpoint del servicio
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); // Método GET

                    // 2. Leer la respuesta del servidor
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder respuesta = new StringBuilder(); // Para almacenar la respuesta
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea); // Construir la respuesta
                    }
                    reader.close(); // Cerrar el lector después de leer

                    // 3. Actualizar la interfaz con la respuesta
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_respuesta_mensaje.setText("Mensaje: " + respuesta.toString()); // Mostrar el mensaje en el TextView
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir errores en consola

                    // Mostrar un mensaje de error en la interfaz
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MENSAJE.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start(); // Iniciar el hilo
    }
}
