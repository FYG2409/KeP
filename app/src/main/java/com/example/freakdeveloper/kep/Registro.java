package com.example.freakdeveloper.kep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.example.freakdeveloper.kep.model.Persona;
import com.example.freakdeveloper.kep.model.Carrera;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Registro extends AppCompatActivity
{
    private EditText NickField, CorField, PassField, PassField2;
    private Button RegistroButton;
    private FirebaseAuth Auth;
    private ProgressDialog Dialog;
    private Spinner EscuelaA, EscuelaI;
    private FirebaseAuth.AuthStateListener AuthListener;
    //PARA FIREBASE
    private DatabaseReference databaseReference;

    private  static final String nodoPersona="Personas";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        IniciarVistas();

        Auth = FirebaseAuth.getInstance();
        Dialog = new ProgressDialog(this);

        //PARA FIREBASE
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        RegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registra();
            }
        });

        AuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

            }
        };
    }

    private void Registra()
    {
        final String name = NickField.getText().toString().trim();
        final String email = CorField.getText().toString().trim();
        final String contra = PassField.getText().toString().trim();
        final String contra2 = PassField2.getText().toString().trim();
        final String EA = EscuelaA.getSelectedItem().toString();
        final String EI = EscuelaI.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(contra) && !TextUtils.isEmpty(contra2))
        {
            Dialog.setMessage("Registrando...");
            Dialog.show();
            //anterior
            if (contra2.equals(contra))
            {
                Task<AuthResult> authResultTask = Auth.createUserWithEmailAndPassword(email, contra)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Auth.signInWithEmailAndPassword(email, contra);
                                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                    //DatabaseReference currentUserDB = Database.child(Auth.getCurrentUser().getUid());
                                    //nuevo

                                    FirebaseUser Usu= Auth.getCurrentUser();
                                    Usu.sendEmailVerification();
                                    String ID = Auth.getCurrentUser().getUid();
                                    Persona persona = new Persona(ID, name, EA, EI,email,contra);
                                    databaseReference.child(nodoPersona).child(persona.getIdPersona()).setValue(persona);
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(Registro.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Dialog.dismiss();

                                } else {
                                    Toast.makeText(Registro.this, "Error En El Registro", Toast.LENGTH_SHORT).show();
                                }

                            }


                        });
            }
        }
    }

    private void IniciarVistas()
    {
        EscuelaA = (Spinner) findViewById(R.id.EActual);
        String[] vocacional = {"CECyT 1", "CECyT 2", "CECyT 3", "CECyT 4", "CECyT 5", "CECyT 6", "CECyT 7", "CECyT 8", "CECyT 9",
                "CECyT 10", "CECyT 11", "CECyT 12", "CECyT 13", "CECyT 14", "CECyT 15", "CECyT 16", "CECyT 17", "CECyT 18", "CET","ENP1","ENP2"
                ,"ENP3","ENP4","ENP5","ENP6","ENP7","ENP8","ENP9","CCH Naucalpan","CCH Vallejo","CCH Azcapotzalco","CCH Oriente","CCH Sur","Otro"};
        EscuelaA.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vocacional));
        //spinner carreras


        DatabaseReference recupera = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoCarrera));
        recupera.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EscuelaI = (Spinner) findViewById(R.id.EIngresar);
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
                EscuelaI.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, superior));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        NickField = (EditText) findViewById(R.id.Nick);
        CorField = (EditText) findViewById(R.id.Cor);
        PassField = (EditText) findViewById(R.id.Pass);
        PassField2 = (EditText) findViewById(R.id.Pass2);
        RegistroButton = (Button) findViewById(R.id.Registro);

    }
}