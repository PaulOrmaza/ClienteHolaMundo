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

public class ISOSCELES extends AppCompatActivity {

    // Referencias a los elementos de la interfaz
    private EditText editBaseMayor, editBaseMenor, editLado, editAltura;
    private Button btnCalcularIsosceles;
    private TextView txtResultadoIsosceles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isosceles);

        // Inicializar vistas
        editBaseMayor = findViewById(R.id.edit_base_mayor);
        editBaseMenor = findViewById(R.id.edit_base_menor);
        editLado = findViewById(R.id.edit_lado);
        editAltura = findViewById(R.id.edit_altura);
        btnCalcularIsosceles = findViewById(R.id.btn_calcular_isosceles);
        txtResultadoIsosceles = findViewById(R.id.txt_resultado_isosceles);

        // Configurar el botón para calcular
        btnCalcularIsosceles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseMayor = editBaseMayor.getText().toString();
                String baseMenor = editBaseMenor.getText().toString();
                String lado = editLado.getText().toString();
                String altura = editAltura.getText().toString();

                // Validar que todos los campos estén llenos
                if (baseMayor.isEmpty() || baseMenor.isEmpty() || lado.isEmpty() || altura.isEmpty()) {
                    Toast.makeText(ISOSCELES.this, "Por favor ingrese todos los valores.", Toast.LENGTH_SHORT).show();
                } else {
                    // Convertir las entradas a tipo double
                    double baseMayorDouble = Double.parseDouble(baseMayor);
                    double baseMenorDouble = Double.parseDouble(baseMenor);
                    double ladoDouble = Double.parseDouble(lado);
                    double alturaDouble = Double.parseDouble(altura);

                    // Obtener los resultados
                    obtenerResultadosIsosceles(baseMayorDouble, baseMenorDouble, ladoDouble, alturaDouble);
                }
            }
        });
    }

    // Método para obtener los resultados del trapecio isósceles desde el servidor
    private void obtenerResultadosIsosceles(double baseMayor, double baseMenor, double lado, double altura) {
        // Crear la URL con los parámetros
        String urlString = "http://192.168.137.79:3003/figura/trapecio/" + baseMayor + "/" + baseMenor + "/" + lado + "/" + altura;

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
                                txtResultadoIsosceles.setText("\nÁrea: " + area + "\nPerímetro: " + perimetro);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ISOSCELES.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir el error en consola para depuración

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ISOSCELES.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start(); // Iniciar el hilo
    }
}
