package com.example.freakdeveloper.kep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.freakdeveloper.kep.model.Carrera;
import com.example.freakdeveloper.kep.model.Persona;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


public class Ajustes extends AppCompatActivity {

    private TextView NickNameA, EmailA, ContraseA, EscuelaA, EscuelaIngA;
    private EditText NickNameN,ContraseN,ContraseConf;
    private Button Cambiar;
    private Spinner EscA, EscI;
    private String Email;
    private  static final String nodoPersona="Personas";
    //TRAER DATOS
    private String uID="";
    private String NickName="";
    private String Correo="";
    private String Contra="";
    private String EActual="";
    private String EIngresar="";

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Persona persona1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        IniciarVista();

        //Trae NickName, Correo, Escuela Actual y Escuela a Ingresar
        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //-----TRAYENDO PERSONA----
        user = FirebaseAuth.getInstance().getCurrentUser();
        Email = user.getEmail();

        databaseReference.child(nodoPersona).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Persona persona = snapshot.getValue(Persona.class);
                        String mail = persona.getEmail();
                        if (mail.equals(Email)) {
                            //SE ENCONTRO LA PERSONA CON EL EMAIL INDICADO
                            uID = persona.getIdPersona();
                            NickName = persona.getNickName();
                            Correo = persona.getEmail();
                            Contra = persona.getContra();
                            EActual = persona.getEscActual();
                            EIngresar = persona.getEscingresar();
                            break;
                        } else {

                        }
                    }

                    NickNameA.setText("NickName Actual: " + NickName);
                    EmailA.setText("Email: " + Correo);
                    ContraseA.setText("Contraseña Actual: " + Contra);
                    EscuelaA.setText("Escuela Actual: " + EActual);
                    EscuelaIngA.setText("Escuela a Ingresar Actual: " + EIngresar);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void IniciarVista()
    {

        EscA = (Spinner) findViewById(R.id.EActual);
        String[] vocacional = {"CECyT 1", "CECyT 2", "CECyT 3", "CECyT 4", "CECyT 5", "CECyT 6", "CECyT 7", "CECyT 8", "CECyT 9",
                "CECyT 10", "CECyT 11", "CECyT 12", "CECyT 13", "CECyT 14", "CECyT 15", "CECyT 16", "CECyT 17", "CECyT 18", "CET","ENP1","ENP2"
                ,"ENP3","ENP4","ENP5","ENP6","ENP7","ENP8","ENP9","CCH Naucalpan","CCH Vallejo","CCH Azcapotzalco","CCH Oriente","CCH Sur","Otro"};
        EscA.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vocacional));
        //spinner carreras


        DatabaseReference recupera = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoCarrera));
        recupera.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EscI = (Spinner) findViewById(R.id.EIngresar);
                ArrayList<String> sup=new ArrayList<>();
                for(DataSnapshot datasnapshot: dataSnapshot.getChildren())
                {
                    Carrera car=datasnapshot.getValue(Carrera.class);
                    String carre=car.getCarrera();
                    sup.add(carre);
                }

                String[] superior= new String[sup.size()];
                for(int i=0;i<sup.size();i++) {
                    superior[i]=sup.get(i);
                }
                EscI.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, superior));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        this.NickNameA = (TextView) findViewById(R.id.NickNA);
        this.EmailA= (TextView) findViewById(R.id.MailA);
        this.ContraseA= (TextView) findViewById(R.id.ContraA);
        this.EscuelaA = (TextView) findViewById(R.id.EscuA);
        this.EscuelaIngA = (TextView) findViewById(R.id.EscuIngA);

        this.NickNameN = (EditText) findViewById(R.id.NickNN);
        this.ContraseN = (EditText) findViewById(R.id.ContraN);
        this.ContraseConf = (EditText) findViewById(R.id.Confir);

        this.Cambiar = (Button) findViewById(R.id.btnCambiar);
    }

    private void Cambiar()
    {
        databaseReference = firebaseDatabase.getInstance().getReference();

        String name = NickNameN.getText().toString().trim();
        String contra = ContraseN.getText().toString().trim();
        String contra2 = ContraseConf.getText().toString().trim();
        String EA = EscA.getSelectedItem().toString();
        String EI = EscI.getSelectedItem().toString();

        if(TextUtils.isEmpty(name))
        {
            name = NickName;
        }

        if(TextUtils.isEmpty(contra)) {
            contra = Contra;
            contra2 = Contra;
        }

        if(!contra.equals(contra2))
        {
            contra = Contra;
            contra2 = Contra;

            databaseReference.child(nodoPersona).child(uID).child("Contra").setValue(contra);
            databaseReference.child(nodoPersona).child(uID).child("EscActual").setValue(EA);
            databaseReference.child(nodoPersona).child(uID).child("Escingresar").setValue(EI);
            databaseReference.child(nodoPersona).child(uID).child("nickName").setValue(name);

            databaseReference.child(nodoPersona).child(uID).child("nickName").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Write was successful!
                    // ...
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                        }
                    });
        }

        NickNameA.setText("NickName Actual: " + name);
        EmailA.setText("Email: " + Email);
        ContraseA.setText("Contraseña Actual: " + contra);
        EscuelaA.setText("Escuela Actual: " + EA);
        EscuelaIngA.setText("Escuela a Ingresar Actual: " + EI);
    }
}
