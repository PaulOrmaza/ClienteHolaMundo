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

public class CIRCULO extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private EditText edt_radio; // Para ingresar el radio
    private Button btn_calcular_circulo; // Botón para hacer la solicitud
    private TextView txt_resultado_area, txt_resultado_perimetro; // Para mostrar el área y el perímetro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circulo); // Usar el nuevo layout para esta actividad

        // Vincular las referencias con los elementos del layout
        edt_radio = findViewById(R.id.edt_radio);
        btn_calcular_circulo = findViewById(R.id.btn_calcular_circulo);
        txt_resultado_area = findViewById(R.id.txt_resultado_area);
        txt_resultado_perimetro = findViewById(R.id.txt_resultado_perimetro);

        // Configurar el botón para hacer la solicitud
        btn_calcular_circulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el radio ingresado y enviarlo como parámetro
                String radio = edt_radio.getText().toString();
                if (!radio.isEmpty()) {
                    realizarCalculoCirculo(radio);
                } else {
                    Toast.makeText(CIRCULO.this, "Por favor ingrese el radio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para realizar la solicitud GET con el radio como parámetro
    private void realizarCalculoCirculo(String radio) {
        // Crear un hilo para realizar la solicitud en segundo plano
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor usando el radio como parámetro en la URL
                    URL url = new URL("http://192.168.1.22:3003/figura/circulo/" + radio); // Usar el radio en la URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); // Método GET

                    // 2. Leer la respuesta del servidor
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    final StringBuilder respuesta = new StringBuilder();
                    String linea;

                    while ((linea = reader.readLine()) != null) {
                        respuesta.append(linea); // Construir la respuesta
                    }
                    reader.close(); // Cerrar el lector después de leer

                    // 3. Actualizar la interfaz con la respuesta
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Parsear la respuesta JSON
                                JSONObject jsonResponse = new JSONObject(respuesta.toString());
                                String area = jsonResponse.getString("area");
                                String perimetro = jsonResponse.getString("perimetro");

                                // Mostrar el área y el perímetro en los TextViews
                                txt_resultado_area.setText("Área: " + area);
                                txt_resultado_perimetro.setText("Perímetro: " + perimetro);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(CIRCULO.this, "Error al parsear los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CIRCULO.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        hilo.start(); // Ejecutar el hilo
    }
}
