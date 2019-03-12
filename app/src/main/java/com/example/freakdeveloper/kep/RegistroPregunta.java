package com.example.freakdeveloper.kep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroPregunta extends AppCompatActivity {

    private EditText idPregunta, materia, pregunta, rA, rB, rC, rD, solucion;
    private String idPreguntaTxt, materiaTxt, preguntaTxt, rATxt, rBTxt, rCTxt, rDTxt, solucionTxt;

    //PARA FIREBASE
    private DatabaseReference databaseReference;

    private  static final String nodoPregunta="Preguntas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pregunta);

        materia = (EditText) findViewById(R.id.materia);
        pregunta = (EditText) findViewById(R.id.pregunta);
        rA = (EditText) findViewById(R.id.rA);
        rB = (EditText) findViewById(R.id.rB);
        rC = (EditText) findViewById(R.id.rC);
        rD = (EditText) findViewById(R.id.rD);
        solucion = (EditText) findViewById(R.id.solucion);

        //PARA FIREBASE
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void creaPregunta(View view){
        materiaTxt = materia.getText().toString();
        preguntaTxt = pregunta.getText().toString();
        rATxt = rA.getText().toString();
        rBTxt = rB.getText().toString();
        rCTxt = rC.getText().toString();
        rDTxt = rD.getText().toString();
        solucionTxt = solucion.getText().toString();

        Pregunta pregunta = new Pregunta(materiaTxt, preguntaTxt, rATxt, rBTxt, rCTxt, rDTxt, solucionTxt);
        databaseReference.child(nodoPregunta).child(databaseReference.push().getKey()).setValue(pregunta);
        Toast.makeText(this, "PREGUNTA REGISTRADA", Toast.LENGTH_SHORT).show();
    }
}
