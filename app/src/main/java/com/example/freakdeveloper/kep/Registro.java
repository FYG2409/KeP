package com.example.freakdeveloper.kep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
<<<<<<< HEAD
=======
import android.widget.Toast;
>>>>>>> 961d81570ea689e4f5219176683231d13eb3d3bf
import android.widget.Spinner;
import android.widget.Toast;

<<<<<<< HEAD
import com.example.freakdeveloper.kep.model.Carrera;
=======
>>>>>>> 961d81570ea689e4f5219176683231d13eb3d3bf
import com.example.freakdeveloper.kep.model.Persona;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
<<<<<<< HEAD
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
=======
import com.google.firebase.auth.FirebaseUser;
>>>>>>> 961d81570ea689e4f5219176683231d13eb3d3bf
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

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener AuthListener;
    //PARA FIREBASE
    private DatabaseReference databaseReference;

    private  static final String nodoPersona="Personas";

    private  static final String nodoPersona="Personas";

    @Override
    protected void onStart()
    {
        super.onStart();
        Auth.addAuthStateListener(AuthListener);
    }


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
                if (firebaseAuth.getCurrentUser() != null)
                {
                    Intent intent = new Intent(Registro.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
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
<<<<<<< HEAD
            //anterior
            if (contra2.equals(contra)) {
                Task<AuthResult> authResultTask = Auth.createUserWithEmailAndPassword(email, contra)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Dialog.dismiss();
                                if (task.isSuccessful()) {
                                    Auth.signInWithEmailAndPassword(email, contra);
                                    //Toast.makeText(ActivityRegister.this, user_id, Toast.LENGTH_SHORT).show();

                                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                    DatabaseReference currentUserDB = Database.child(Auth.getCurrentUser().getUid());
                                    //nuevo
                                    Auth.createUserWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Dialog.dismiss();
                                            if (task.isSuccessful()) {

                                                Auth.signInWithEmailAndPassword(email, contra);
                                                String ID = Auth.getCurrentUser().getUid();
                                                Persona persona = new Persona(ID, name, EA, EI,email,contra);
                                                databaseReference.child(nodoPersona).child(persona.getIdPersona()).setValue(persona);

                                            } else {
                                                Toast.makeText(Registro.this, "Error En El Registro", Toast.LENGTH_SHORT).show();
=======
            if (contra2.equals(contra))
            {

                Auth.createUserWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Dialog.dismiss();
                        if (task.isSuccessful())
                        {

                            Auth.signInWithEmailAndPassword(email, contra);
                            String ID= Auth.getCurrentUser().getUid();
                            Persona persona = new Persona(ID , name, EA, EI , email , contra);
                            databaseReference.child(nodoPersona).child(persona.getIdPersona()).setValue(persona);
                            FirebaseUser Usuario= Auth.getCurrentUser();

                            Usuario.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(Registro.this, "Por favor Revisa tu correo", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(Registro.this, "Error De Verificación", Toast.LENGTH_SHORT).show();
>>>>>>> 961d81570ea689e4f5219176683231d13eb3d3bf
                                            }
                                        }
                                    });

<<<<<<< HEAD
                                    Dialog.setMessage("Registrando...");
                                    Dialog.show();
                                }

                            }
                        });
=======

                        }
                        else
                        {
                            Toast.makeText(Registro.this, "Error En El Registro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Dialog.setMessage("Registrando...");
                Dialog.show();
>>>>>>> 961d81570ea689e4f5219176683231d13eb3d3bf
            }
        }
    }

    private void IniciarVistas()
    {
        EscuelaA = (Spinner) findViewById(R.id.EActual);
        String[] vocacional = {"Escuela Actual", "CECyT 1", "CECyT 2", "CECyT 3", "CECyT 4", "CECyT 5", "CECyT 6", "CECyT 7", "CECyT 8", "CECyT 9",
                "CECyT 10", "CECyT 11", "CECyT 12", "CECyT 13", "CECyT 14", "CECyT 15", "CECyT 16", "CECyT 17", "CECyT 18", "CET","ENP1","ENP2"
                ,"ENP3","ENP4","ENP5","ENP6","ENP7","ENP8","ENP9","CCH Naucalpan","CCH Vallejo","CCH Azcapotzalco","CCH Oriente","CCH Sur","Otro"};
<<<<<<< HEAD
        EscuelaA.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, vocacional));
        //spinner carreras

=======
        EscuelaA.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vocacional));
>>>>>>> 961d81570ea689e4f5219176683231d13eb3d3bf

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
                EscuelaI.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, superior));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //String[] superior=sup;
                /*{"Escuela a Igresar","UPIBI","UPIIZ Campus Zacatecas",
                "UPIITA","ENCB","UPIIG Campus Guanajuato","ESIA Unidad Zacatenco",
                "ESIME Unidad Zacatenco","ESIME Unidad Ticomán","ESIME Unidad Culhuacán",
                "UPIICSA","ESIQIE","ESIME Unidad Azcapotzalco","UPIIH Campus Hidalgo",
                "ESCOM","ESIA Unidad Ticomán","ESFM","ESIT","ESIA Unidad Tecamachalco",
                "CICS Unidad Milpa Alta","CICS Unidad Santo Tomás","ESEO","ENMyH",
                "ESM","ESCA Unidad Santo Tomás","ESCA Unidad Tepepan","ESE","EST"};*/


        NickField = (EditText) findViewById(R.id.Nick);
        CorField = (EditText) findViewById(R.id.Cor);
        PassField = (EditText) findViewById(R.id.Pass);
        PassField2 = (EditText) findViewById(R.id.Pass2);
        RegistroButton = (Button) findViewById(R.id.Registro);

    }
}