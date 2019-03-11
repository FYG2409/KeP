package com.example.freakdeveloper.kep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registro extends AppCompatActivity {

    private EditText NickField, CorField, PassField, PassField2;
    private Button RegistroButton;
    private FirebaseAuth Auth;
    private ProgressDialog Dialog;
    private Spinner EscuelaA, EscuelaI;
    private FirebaseAuth.AuthStateListener AuthListener;

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
            if (contra2.equals(contra))
            {
                Dialog.setMessage("Registrando...");
                Dialog.show();
                Auth.createUserWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Dialog.dismiss();
                                if (task.isSuccessful())
                                {
                                    Auth.signInWithEmailAndPassword(email, contra);

                                    DatabaseReference Database = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                                    DatabaseReference currentUserDB = Database.child(Auth.getCurrentUser().getUid());
                                    currentUserDB.child("NickName").setValue(name);
                                    currentUserDB.child("Escuela_Actual").setValue(EA);
                                    currentUserDB.child("Escuela_Ingreso").setValue(EI);
                                }
                                else
                                {
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
        String[] vocacional = {"Escuela Actual", "CECyT 1", "CECyT 2", "CECyT 3", "CECyT 4", "CECyT 5", "CECyT 6", "CECyT 7", "CECyT 8", "CECyT 9",
                "CECyT 10", "CECyT 11", "CECyT 12", "CECyT 13", "CECyT 14", "CECyT 15", "CECyT 16", "CECyT 17", "CECyT 18", "CET"};
        EscuelaA.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vocacional));

        EscuelaI = (Spinner) findViewById(R.id.EIngresar);
        String[] superior={"Escuela a Igresar","UPIBI","UPIIZ Campus Zacatecas",
                "UPIITA","ENCB","UPIIG Campus Guanajuato","ESIA Unidad Zacatenco",
                "ESIME Unidad Zacatenco","ESIME Unidad Ticomán","ESIME Unidad Culhuacán",
                "UPIICSA","ESIQIE","ESIME Unidad Azcapotzalco","UPIIH Campus Hidalgo",
                "ESCOM","ESIA Unidad Ticomán","ESFM","ESIT","ESIA Unidad Tecamachalco",
                "CICS Unidad Milpa Alta","CICS Unidad Santo Tomás","ESEO","ENMyH",
                "ESM","ESCA Unidad Santo Tomás","ESCA Unidad Tepepan","ESE","EST"};
        EscuelaI.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, superior));

        NickField = (EditText) findViewById(R.id.Nick);
        CorField = (EditText) findViewById(R.id.Cor);
        PassField = (EditText) findViewById(R.id.Pass);
        PassField2 = (EditText) findViewById(R.id.Pass2);
        RegistroButton = (Button) findViewById(R.id.Registro);

    }
}
