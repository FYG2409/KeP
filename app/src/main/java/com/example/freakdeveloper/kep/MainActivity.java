package com.example.freakdeveloper.kep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void irAInicioSesion(View view){
        Intent intent = new Intent(this, IniciaSesion.class);
        startActivity(intent);
    }

    public void irARegistro(View view){
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    public void irAPrincipal(View view){
        Intent intent = new Intent(this, NavigationDrawe.class);
        startActivity(intent);
    }

    public void irARegPregunta(View view){
        Intent intent = new Intent(this, RegistroPregunta.class);
        startActivity(intent);
    }
}
