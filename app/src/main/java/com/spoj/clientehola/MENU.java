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



    }
}
