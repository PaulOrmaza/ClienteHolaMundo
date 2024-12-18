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

public class SUMASIMPLE extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private TextView txt_resultado_suma; // Para mostrar el resultado
    private Button btn_suma_simple;   // Botón para enviar solicitud

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumasimple); // Usar el nuevo layout para esta actividad

        // Vincular las referencias con los elementos del layout
        txt_resultado_suma = findViewById(R.id.txt_resultado_suma);
        btn_suma_simple = findViewById(R.id.btn_suma_simple);

        // Configuración del botón para realizar la solicitud
        btn_suma_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarSumaSimple(); // Llamada al método que realiza la solicitud
            }
        });
    }

    // Método que realiza la solicitud al servidor
    private void realizarSumaSimple() {
        // Crear un hilo para la solicitud
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor
                    URL url = new URL("http://192.168.137.79:3003/suma"); // Dirección del servicio /suma
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
                            txt_resultado_suma.setText("Resultado: " + respuesta.toString()); // Mostrar la respuesta
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir errores en consola

                    // Mostrar un mensaje de error en la interfaz
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SUMASIMPLE.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start(); // Iniciar el hilo
    }
}
