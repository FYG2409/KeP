package com.example.freakdeveloper.kep;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Preguntas extends AppCompatActivity {

    private String materiaSeleccionada;

    //MIS VARIABLES

    private TextView pregunta;
    private Button rA, rB, rC, rD, siguiente;
    private String solucion;
    private String rATxt, rBTxt, rCTxt, rDTxt, preguntaTxt;
    private int contaBuenas, contaMalas, contaTotal;

    private ArrayList<Pregunta> arrayPregunta = new ArrayList<>();
    private  static final String nodoPregunta="Preguntas";
    private  static final String nodoDuelos="Duelos";

    //PARA TIMER
    private TextView countdownText;

    private CountDownTimer countDownTimer;
    private long tiempoEnMilisegundos = 10000; //Son los minutos que queremos en milisegundos

    private String codigoDuelo, tipoPersona, email;

    //PARA FIREBASE
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        recuperaDatosIntent();

        pregunta = (TextView) findViewById(R.id.pregunta);
        rA = (Button) findViewById(R.id.rA);
        rB = (Button) findViewById(R.id.rB);
        rC = (Button) findViewById(R.id.rC);
        rD = (Button) findViewById(R.id.rD);
        siguiente = (Button) findViewById(R.id.siguiente);

        contaBuenas = 0;
        contaMalas = 0;
        contaTotal = 0;

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        traePreguntas();

        rA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rA);
            }
        });

        rB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rB);
            }
        });

        rC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rC);
            }
        });

        rD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rD);
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeoSiguiente();
            }
        });


    }

    public void iniciaTimer(){
        countDownTimer = new CountDownTimer(tiempoEnMilisegundos, 1000) {
            @Override
            public void onTick(long lon) {
                tiempoEnMilisegundos = lon;
                updateTimer();
            }
            @Override
            public void onFinish() {
                //AQUI TERMINA EL TIEMPO
                if(tipoPersona.equals("Uno")){
                    databaseReference.child(nodoDuelos).child(codigoDuelo).child("totalBuenasUno").setValue(contaBuenas);
                }else
                    if(tipoPersona.equals("Dos")){
                        databaseReference.child(nodoDuelos).child(codigoDuelo).child("totalBuenasDos").setValue(contaBuenas);
                    }else
                        Log.w("Preguntas", "Tipo de persona desconocido");

                verGanador();
            }
        }.start();
     }

     public void verGanador(){
         Intent intent = new Intent(this, muestraDatos.class);
         intent.putExtra("codigoDuelo", codigoDuelo);
         startActivity(intent);
         finish();
     }

    public void updateTimer(){
        int minutos = (int) tiempoEnMilisegundos / 60000;
        int segundos = (int) tiempoEnMilisegundos % 60000 / 1000;

        String timeLeftText;

        timeLeftText = ""+minutos;
        timeLeftText += ":";
        if(segundos<10) timeLeftText += 0;
        timeLeftText += segundos;
        countdownText.setText(timeLeftText);
    }

    public void traePreguntas(){
        databaseReference.child(nodoPregunta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayPregunta.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Pregunta preguntaa = snapshot.getValue(Pregunta.class);
                        if(preguntaa.getMateria().equalsIgnoreCase(materiaSeleccionada)){
                            arrayPregunta.add(preguntaa);
                        }else
                            if(materiaSeleccionada.equals("todas")){
                                arrayPregunta.add(preguntaa);
                            }else
                                Log.w("Preguntas", "No hay preguntas de esta materia en la base");
                    }
                    cargaPregunta(0);
                    Log.w("Preguntas", "Termine de agregar");
                }else
                    Toast.makeText(Preguntas.this, "No hay preguntas en la base", Toast.LENGTH_SHORT).show();
                    Log.w("Preguntas", "No hay preguntas en la base");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cargaPregunta(int index){
        pregunta.setText(arrayPregunta.get(index).getPregunta());
        rA.setText(arrayPregunta.get(index).getrA());
        rB.setText(arrayPregunta.get(index).getrB());
        rC.setText(arrayPregunta.get(index).getrC());
        rD.setText(arrayPregunta.get(index).getrD());
        solucion = arrayPregunta.get(index).getSolucion();
    }

    public void clickeo(View v){
        if(v.getTag().toString().equals(solucion)){
            //Si la contesto bien
            contaBuenas = contaBuenas + 1;
            contaTotal = contaTotal + 1;
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else {
            //Si la contesto mal
            contaMalas = contaMalas + 1;
            contaTotal = contaTotal + 1;
            v.setBackgroundColor(getResources().getColor(R.color.negro));
            if (solucion.equals("A")) {
                rA.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("B")) {
                rB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("C")) {
                rC.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("D")) {
                rD.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
        siguiente.setEnabled(true);
    }

    public void clickeoSiguiente(){
        if(arrayPregunta.size()>contaTotal){
            limpiaColor();
            //Enviando nueva pregunta
            cargaPregunta(contaTotal);
            siguiente.setEnabled(false);
        }else{
            Toast.makeText(this, "Ya has contestado todas las preguntas", Toast.LENGTH_SHORT).show();
            onStop();
        }
    }

    public void limpiaColor(){
        rA.setBackgroundColor(getResources().getColor(R.color.blanco));
        rB.setBackgroundColor(getResources().getColor(R.color.blanco));
        rC.setBackgroundColor(getResources().getColor(R.color.blanco));
        rD.setBackgroundColor(getResources().getColor(R.color.blanco));
    }

    public void recuperaDatosIntent(){
        Bundle extras = getIntent().getExtras();
        materiaSeleccionada = extras.getString("materia");
        email = extras.getString("email");

        tipoPersona = extras.getString("tipoPersona");
        codigoDuelo = extras.getString("codigoDuelo");
        if(materiaSeleccionada.equals("todas")){
            ponTimer();
        }
    }

    public void ponTimer(){
        countdownText = (TextView) findViewById(R.id.countdown_text);
        countdownText.setVisibility(View.VISIBLE);
        iniciaTimer();
    }


}