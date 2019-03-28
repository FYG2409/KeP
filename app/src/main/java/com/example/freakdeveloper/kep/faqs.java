package com.example.freakdeveloper.kep;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.freakdeveloper.kep.model.faq;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class faqs extends AppCompatActivity {

    LinearLayout contePreguntas;
    private ArrayList<faq> faqs = new ArrayList<>();
    private int conta = 0;

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private static final String nodoFAQS = "FAQS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        contePreguntas = (LinearLayout) findViewById(R.id.contePreguntas);

        conta = 0;

        traePreguntas();
    }

    public void enviaPreguntas(){

        for(int i = 0; i<faqs.size(); i++){
            TextView pregunta = new TextView(getApplicationContext(), null, 0, R.style.tvPregunta);
            final TextView respuesta = new TextView(getApplicationContext(), null, 0, R.style.tvRespuesta);
            RelativeLayout rela = new RelativeLayout(getApplicationContext());
            rela.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 20));


            //ENVIANDO DATOS
            pregunta.setText(faqs.get(i).getPregunta());
            respuesta.setText(faqs.get(i).getRespuesta());

            contePreguntas.addView(pregunta);
            respuesta.setVisibility(View.GONE);
            contePreguntas.addView(respuesta);
            contePreguntas.addView(rela);


            pregunta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    muestraRespuesta(respuesta);
                }
            });
        }

    }

    public void muestraRespuesta(TextView res){
        if(conta == 0) {
            res.setVisibility(View.VISIBLE);
            conta = 1;
        }else
            if(conta == 1){
                res.setVisibility(View.GONE);
                conta = 0;
            }
    }

    public void traePreguntas(){
        databaseReference.child(nodoFAQS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        faq faq = snapshot.getValue(com.example.freakdeveloper.kep.model.faq.class);
                        //AQUI INICIO A PONER LAS PREGUNTAS
                        faqs.add(faq);
                        //AQUI TERMINO DE PONER LAS PREGUNTAS
                    }
                    enviaPreguntas();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
