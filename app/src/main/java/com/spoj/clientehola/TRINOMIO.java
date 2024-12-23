package com.spoj.clientehola;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TRINOMIO extends AppCompatActivity {

    private EditText editA, editB, editC, editX;
    private Spinner spinnerOperador;
    private Button btnCalcularTrinomio;
    private TextView txtResultadoTrinomio, txtSolucionesTrinomio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trinomio);

        // Inicializar vistas
        editA = findViewById(R.id.edit_a);
        editB = findViewById(R.id.edit_b);
        editC = findViewById(R.id.edit_c);
        editX = findViewById(R.id.edit_x);
        spinnerOperador = findViewById(R.id.spinner_operador);
        btnCalcularTrinomio = findViewById(R.id.btn_calcular_trinomio);
        txtResultadoTrinomio = findViewById(R.id.txt_resultado_trinomio);
        txtSolucionesTrinomio = findViewById(R.id.txt_soluciones_trinomio);

        // Configurar botón
        btnCalcularTrinomio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularSegunEntrada();
            }
        });

        // Listener para cambios en el campo "editX"
        editX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtResultadoTrinomio.setText(""); // Limpiar resultado
                txtSolucionesTrinomio.setText(""); // Limpiar soluciones
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void calcularSegunEntrada() {
        String a = editA.getText().toString();
        String b = editB.getText().toString();
        String operador = spinnerOperador.getSelectedItem().toString();
        String c = editC.getText().toString();
        String x = editX.getText().toString();

        if (a.isEmpty() || b.isEmpty()) {
            Toast.makeText(TRINOMIO.this, "Por favor ingrese los valores de 'a' y 'b'.", Toast.LENGTH_SHORT).show();
            return;
        }

        double valorA = Double.parseDouble(a);
        double valorB = Double.parseDouble(b);
        Double valorC = c.isEmpty() ? null : Double.parseDouble(c);
        Double valorX = x.isEmpty() ? null : Double.parseDouble(x);

        calcularTrinomio(valorA, valorB, operador, valorC, valorX);
    }

    private void calcularTrinomio(double a, double b, String operador, Double c, Double x) {
        String urlString = "http://192.168.1.25:3003/trinomio/" + a + "/" + b + "/" + operador +
                (c != null ? "/" + c : "") + (x != null ? "/" + x : "");

        Thread hilo = new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuilder respuesta = new StringBuilder();
                String linea;

                while ((linea = reader.readLine()) != null) {
                    respuesta.append(linea);
                }
                reader.close();

                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(respuesta.toString());
                        String desarrollo = jsonObject.getString("desarrollo");
                        String resultadoFinal = jsonObject.getString("resultadoFinal");

                        txtResultadoTrinomio.setText("Desarrollo: " + desarrollo + "\nResultado: " + resultadoFinal);

                        if (x != null) {
                            String ecuacion = jsonObject.getString("ecuacion");
                            JSONObject solucion = jsonObject.optJSONObject("solucionEcuacion");

                            txtSolucionesTrinomio.setText("Ecuación: " + ecuacion + "\nSoluciones: " +
                                    (solucion != null ? solucion.toString() : "No hay soluciones reales."));
                        } else {
                            txtSolucionesTrinomio.setText(""); // Limpiar soluciones si no hay x
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(TRINOMIO.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(TRINOMIO.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                });
            }
        });

        hilo.start();
    }
}
