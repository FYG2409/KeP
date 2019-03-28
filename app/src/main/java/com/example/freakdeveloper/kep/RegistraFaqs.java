package com.example.freakdeveloper.kep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freakdeveloper.kep.model.faq;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistraFaqs extends AppCompatActivity {

    private EditText pregunta, respuesta;
    private String txtPregunta, txtRepuesta;

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private static final String nodoFAQS = "FAQS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_faqs);

        pregunta = (EditText) findViewById(R.id.pregunta);
        respuesta = (EditText) findViewById(R.id.respuesta);

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void registra(View view){
        txtPregunta = pregunta.getText().toString();
        txtRepuesta = respuesta.getText().toString();

        faq faq = new faq(txtPregunta, txtRepuesta);

        String xId = databaseReference.push().getKey();

        databaseReference.child(nodoFAQS).child(xId).setValue(faq);

        Toast.makeText(this, "FAQ REGISTRADA", Toast.LENGTH_SHORT).show();

    }
}
