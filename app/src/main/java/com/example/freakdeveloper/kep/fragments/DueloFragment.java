package com.example.freakdeveloper.kep.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freakdeveloper.kep.Preguntas;
import com.example.freakdeveloper.kep.R;
import com.example.freakdeveloper.kep.model.Duelo;
import com.example.freakdeveloper.kep.model.Persona;
import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DueloFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //MIS VARIABLES
    private EditText codigoIngresado;
    private TextView codigo, esperando;
    private String NickName, email;
    private Button enviaCodigo, creaDuelo;
    private  static final String nodoPersona="Personas";
    private  static final String nodoDuelos="Duelos";
    private String codigoDuelo;
    private int conta;

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public DueloFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DueloFragment newInstance(String param1, String param2) {
        DueloFragment fragment = new DueloFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duelo, container, false);

        codigoIngresado = (EditText) view.findViewById(R.id.codigoIngresado);
        codigo = (TextView) view.findViewById(R.id.codigo);
        enviaCodigo = (Button) view.findViewById(R.id.enviaCodigo);
        esperando = (TextView) view.findViewById(R.id.esperando);
        creaDuelo = (Button) view.findViewById(R.id.creaDuelo);

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //-----AQUI----
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                email = firebaseUser.getEmail();
            }
        };

        databaseReference.child(nodoPersona).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Persona persona = snapshot.getValue(Persona.class);
                        if(persona.getEmail().equals(email)){
                            //SE ENCONTRO LA PERSONA CON EL EMAIL INDICADO
                            NickName = persona.getNickName();
                            codigoDuelo = "K"+NickName+"P";
                            codigo.setText(codigoDuelo);
                            Log.w("HOLAWAS", "Nick: "+NickName);
                            Log.w("HOLAWAS", "Codigo: "+codigoDuelo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //-------------

        //CONSTANTE NICKNAME
        //NickName = "yash9130";
        //email = "yash@gmail.com";


        creaDuelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creaDuelo(creaDuelo);
            }
        });

        enviaCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaCodigo(enviaCodigo);
            }
        });
        return view;
    }

    public void validaCodigo(View view){
        final String codigoIngresadoTxt = codigoIngresado.getText().toString();

        if(codigoIngresadoTxt.equals(codigoDuelo)){
            Toast.makeText(getContext(), "Ese codigo es tuyo", Toast.LENGTH_SHORT).show();
        }else {
            databaseReference.child(nodoDuelos).child(codigoIngresadoTxt).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //Si existe el codigo
                        Duelo duelo = dataSnapshot.getValue(Duelo.class);
                        if(duelo.getCorreoPerDos()==null){
                            //LA SEGUNDA PERSONA ESTA DISPONIBLE
                            iniciaDuelo(codigoIngresadoTxt);
                        }else{
                            //LA SEGUNDA PERSONA NO ESTA DISPONIBLE
                            Toast.makeText(getContext(), "El codigo esta en uso", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //No existe el codigo
                        Toast.makeText(getContext(), "No existe el codigo", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void iniciaDuelo(String codigoIngresadoTxt){
        databaseReference.child(nodoDuelos).child(codigoIngresadoTxt).child("correoPerDos").setValue(email);
        Intent intent = new Intent(getContext(), Preguntas.class);
        intent.putExtra("materia", "todas");
        intent.putExtra("codigoDuelo", codigoIngresadoTxt);
        intent.putExtra("tipoPersona", "Dos");
        startActivity(intent);
    }


    public void creaDuelo(View view){
        conta=0;
        Duelo duelo = new Duelo(email, null, null, null);
        databaseReference.child(nodoDuelos).child(codigoDuelo).setValue(duelo);
        esperando.setVisibility(View.VISIBLE);

        databaseReference.child(nodoDuelos).child(codigoDuelo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Si existe el codigo
                    Duelo duelo = dataSnapshot.getValue(Duelo.class);
                    if(duelo.getCorreoPerDos()==null){
                        //LA SEGUNDA PERSONA ESTA DISPONIBLE
                        esperando.setVisibility(View.VISIBLE);
                    }else{
                        conta = conta + 1;
                        if(conta==1){
                            Log.w("HOLAWAS", "INICIE ACTIVITY");
                            //LA SEGUNDA PERSONA NO ESTA DISPONIBLE
                            esperando.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getContext(), Preguntas.class);
                            intent.putExtra("materia", "todas");
                            intent.putExtra("codigoDuelo",codigoDuelo);
                            intent.putExtra("tipoPersona", "Uno");
                            intent.putExtra("email", email);
                            startActivity(intent);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


    public void traePersona(){




    }


}
