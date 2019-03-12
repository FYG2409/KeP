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
    private TextView codigo;
    private String NickName, email;
    private Button generaCodigo, enviaCodigo;
    //private  static final String nodoPersona="Usuarios";
    private  static final String nodoDuelos="Duelos";

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    //private FirebaseAuth.AuthStateListener authStateListener;
    //private FirebaseAuth firebaseAuth;

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

        //CONSTANTE NICKNAME
        NickName = "yash9130";
        email = "yash@gmail.com";


        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //firebaseAuth = FirebaseAuth.getInstance();

        generaCodigo = (Button) view.findViewById(R.id.generaCodigo);
        codigoIngresado = (EditText) view.findViewById(R.id.codigoIngresado);
        codigo = (TextView) view.findViewById(R.id.codigo);
        enviaCodigo = (Button) view.findViewById(R.id.enviaCodigo);
        generaCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creaCodigo(generaCodigo);
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

        databaseReference.child(nodoDuelos).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Duelo duelo = snapshot.getValue(Duelo.class);
                        if(duelo.getCorreoPerDos()==null){
                            actualizaDuelo(codigoIngresadoTxt);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void actualizaDuelo(String codigoIngresadoTxt){
        databaseReference.child(nodoDuelos).child(codigoIngresadoTxt).child("correoPerDos").setValue(email);
        Intent intent = new Intent(getContext(), Preguntas.class);
        intent.putExtra("materia", "todas");
        startActivity(intent);
        Log.w("FEIKS", "ABRI ACTIVITY");
    }

    public void creaCodigo(View view){
        String codigoAleatorio = "K"+NickName+"P";
        codigo.setText(codigoAleatorio);

        Duelo duelo = new Duelo(email, null, null, codigoAleatorio);
        databaseReference.child(nodoDuelos).child(codigoAleatorio).setValue(duelo);
        Toast.makeText(getContext(), "DUELO INICIADO", Toast.LENGTH_SHORT).show();

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
/*
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
    */
}
