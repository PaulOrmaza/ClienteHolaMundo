package com.spoj.clientehola;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MENU extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Botón 1: Ver Lista de Nombres (MainActivity ya existe)
        Button btnVerLista = findViewById(R.id.btn_ver_lista);
        btnVerLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Botón 2: Mensaje de Paul
        Button btnMensaje = findViewById(R.id.btn_ver_paul);
        btnMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, MENSAJE.class);
                startActivity(intent);
            }
        });


        // Botón 3: Suma Simple
        Button btnSumaSimple = findViewById(R.id.btn_suma_simple);
        btnSumaSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, SUMASIMPLE.class);
                startActivity(intent);
            }
        });

        // Botón 4: Suma con Parámetro
        Button btnSumaParametro = findViewById(R.id.btn_suma_parametro);
        btnSumaParametro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, SUMAPARAMETRO.class);
                startActivity(intent);
            }
        });

        // Botón 5: Círculo (Área y Perímetro)
        Button btnCirculo = findViewById(R.id.btn_calcular_circulo);
        btnCirculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, CIRCULO.class);
                startActivity(intent);
            }
        });

        // Botón 6: Pentágono (Área y Perímetro)
        Button btnPentagono = findViewById(R.id.btn_calcular_pentagono);
        btnPentagono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, PENTAGONO.class);
                startActivity(intent);
            }
        });

        // Botón 7: Isósceles (Área y Perímetro)
        Button btnIsosceles = findViewById(R.id.btn_calcular_trapecio);
        btnIsosceles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, ISOSCELES.class);
                startActivity(intent);
            }
        });

        Button btnTrinomio = findViewById(R.id.btn_Trinomio);
        btnTrinomio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MENU.this, TRINOMIO.class);
                startActivity(intent);
            }
        });

    }
}
