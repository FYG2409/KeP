
package com.example.freakdeveloper.kep;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freakdeveloper.kep.model.Duelo;
import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class muestraDatos extends AppCompatActivity {

    private Button listo;
    private TextView perUno, perDos;
    private String codigoDuelo;
    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private  static final String nodoDuelos="Duelos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muestra_datos);

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        listo = (Button) findViewById(R.id.listo);
        perUno = (TextView) findViewById(R.id.perUno);
        perDos = (TextView) findViewById(R.id.perDos);

        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cierraVentana();
            }
        });
        recuperaIntent();
    }

    public void recuperaIntent(){
        Bundle extras = getIntent().getExtras();
        codigoDuelo = extras.getString("codigoDuelo");
        traeDuelo();
    }

    public void traeDuelo(){

        databaseReference.child(nodoDuelos).child(codigoDuelo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w("PADANDO", "entre");
                if(dataSnapshot.exists()){
                    Log.w("PADANDO", "PASO");
                    //Si existe el codigo
                    Duelo duelo = dataSnapshot.getValue(Duelo.class);
                    Long contaBuenasUno = duelo.getTotalBuenasUno();
                    Long contaBuenasDos = duelo.getTotalBuenasDos();

                    String contaBuenasUnoTxt = String.valueOf(contaBuenasUno);
                    String contaBuenasDosTxt = String.valueOf(contaBuenasDos);

                    Log.w("PADANDO", "BUENAS 1 " + contaBuenasUnoTxt);
                    Log.w("PADANDO", "BUENAS 2 " + contaBuenasDosTxt);


                    if(contaBuenasUnoTxt.equals("null")||contaBuenasDosTxt.equals("null")){
                        perUno.setText("ESPERA");
                        perDos.setText("ESPERA");
                    }else{
                        listo.setVisibility(View.VISIBLE);
                        if(contaBuenasUno>contaBuenasDos){
                            perUno.setText("GANASTE");
                            perDos.setText("PERDISTE");
                        }else
                            if(contaBuenasDos>contaBuenasUno){
                                perDos.setText("GANASTE");
                                perUno.setText("PERDISTE");
                            }else
                                if(contaBuenasDos == contaBuenasUno){
                                    perUno.setText("EMPATE");
                                    perDos.setText("EMPATE");
                                }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cierraVentana(){
        eliminaDatos();
        finish();
    }

    public void eliminaDatos(){
        databaseReference.child(nodoDuelos).child(codigoDuelo).removeValue();
    }
}
