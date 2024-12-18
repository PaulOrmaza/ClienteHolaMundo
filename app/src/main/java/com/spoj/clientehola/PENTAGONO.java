package com.spoj.clientehola;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PENTAGONO extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private EditText editLado, editApotema;
    private Button btnCalcularPentagono;
    private TextView txtResultadoPentagono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pentagono);

        // Inicializar vistas
        editLado = findViewById(R.id.edit_lado);
        editApotema = findViewById(R.id.edit_apotema);
        btnCalcularPentagono = findViewById(R.id.btn_calcular_pentagono);
        txtResultadoPentagono = findViewById(R.id.txt_resultado_pentagono);

        // Configurar el botón para calcular
        btnCalcularPentagono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lado = editLado.getText().toString();
                String apotema = editApotema.getText().toString();

                // Validar que ambos campos no estén vacíos
                if (lado.isEmpty() || apotema.isEmpty()) {
                    Toast.makeText(PENTAGONO.this, "Por favor ingrese ambos valores.", Toast.LENGTH_SHORT).show();
                } else {

                    double ladoDouble = Double.parseDouble(lado);
                    double apotemaDouble = Double.parseDouble(apotema);
                    obtenerResultadosPentagono(ladoDouble, apotemaDouble);
                }
            }
        });
    }

    // Método para obtener los resultados del pentágono desde el servidor
    private void obtenerResultadosPentagono(double lado, double apotema) {
        // Crear la URL con los parámetros
        String urlString = "http://192.168.1.22:3003/figura/pentagono/" + lado + "/" + apotema;

        // Realizar la solicitud en un hilo secundario para no bloquear la UI
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 2. Leer la respuesta del servidor
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder respuesta = new StringBuilder(); // Almacena toda la respuesta del servidor
                    String linea;

                    // Mientras haya contenido, añadirlo al StringBuilder
                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea);
                    }
                    reader.close(); // Cerramos el lector después de leer

                    // Procesar la respuesta en el hilo principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Convertir la respuesta en un JSONObject
                                JSONObject jsonObject = new JSONObject(respuesta.toString());

                                // Extraer los datos

                                double area = jsonObject.getDouble("area");
                                double perimetro = jsonObject.getDouble("perimetro");

                                // Mostrar los resultados en la interfaz de usuario
                                txtResultadoPentagono.setText("\nÁrea: " + area + "\nPerímetro: " + perimetro);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(PENTAGONO.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir el error en consola para depuración

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PENTAGONO.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start(); // Iniciar el hilo
    }
}


