package com.example.freakdeveloper.kep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private Button regPregunta, regCarrera;
    private int conta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regPregunta = findViewById(R.id.regPregunta);
        regCarrera = findViewById(R.id.regCarrera);

    }

    public void visualiza(View view){
        if(conta==0){
            regPregunta.setVisibility(View.VISIBLE);
            regCarrera.setVisibility(View.VISIBLE);
            conta = 1;
        }else if (conta == 1) {
            regPregunta.setVisibility(View.GONE);
            regCarrera.setVisibility(View.GONE);
            conta = 0;
        }
    }


    public void irAInicioSesion(View view){
        Intent intent = new Intent(this, IniciaSesion.class);
        startActivity(intent);
    }

    public void irARegistro(View view){
        Intent intent = new Intent(this , Registro.class);
        startActivity(intent);
    }

    public void irARegPregunta(View view){
        Intent intent = new Intent(this, RegistroPregunta.class);
        startActivity(intent);
    }

    public void irARegCarrera(View view){
        Intent intent = new Intent(this, RegistroCarrera.class);
        startActivity(intent);
    }
}
