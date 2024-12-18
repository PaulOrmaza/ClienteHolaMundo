package com.spoj.clientehola;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SUMAPARAMETRO extends AppCompatActivity {


    private EditText edt_numero; // Para ingresar el número
    private Button btn_suma_parametro; // Botón para hacer la solicitud
    private TextView txt_resultado_suma_parametro; // Para mostrar el resultado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumaparametro); // Usar el nuevo layout para esta actividad

        // Vincular las referencias con los elementos del layout
        edt_numero = findViewById(R.id.edt_numero);
        btn_suma_parametro = findViewById(R.id.btn_suma_parametro);
        txt_resultado_suma_parametro = findViewById(R.id.txt_resultado_suma_parametro);

        // Configurar el botón para hacer la solicitud
        btn_suma_parametro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el número ingresado y enviarlo como parámetro
                String numero = edt_numero.getText().toString();
                if (!numero.isEmpty()) {
                    realizarSumaConParametro(numero);
                } else {
                    Toast.makeText(SUMAPARAMETRO.this, "Por favor ingrese un número", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para realizar la solicitud GET con el número como parámetro
    private void realizarSumaConParametro(String numero) {
        // Crear un hilo para realizar la solicitud en segundo plano
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. Conexión con el servidor usando el número como parámetro en la URL
                    URL url = new URL("http://192.168.1.22:3003/suma/" + numero); // Usar el número en la URL
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
                            // Mostrar el resultado en el TextView
                            txt_resultado_suma_parametro.setText("Resultado: " + respuesta.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace(); // Imprimir errores en consola

                    // Mostrar un mensaje de error en la interfaz
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SUMAPARAMETRO.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        hilo.start(); // Iniciar el hilo
    }
}