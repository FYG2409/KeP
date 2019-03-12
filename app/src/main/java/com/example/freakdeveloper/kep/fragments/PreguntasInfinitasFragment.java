package com.example.freakdeveloper.kep.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freakdeveloper.kep.Preguntas;
import com.example.freakdeveloper.kep.R;
import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreguntasInfinitasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //MIS VARIABLES

    private TextView pregunta;
    private Button rA, rB, rC, rD, siguiente;
    private String solucion;
    private String rATxt, rBTxt, rCTxt, rDTxt, preguntaTxt;
    private int contaBuenas, contaMalas, contaTotal;

    private ArrayList<Pregunta> arrayPregunta = new ArrayList<>();
    private  static final String nodoPregunta="Preguntas";

    //PARA FIREBASE
    private DatabaseReference databaseReference;




    public PreguntasInfinitasFragment() {
        // Required empty public constructor
    }

    public static PreguntasInfinitasFragment newInstance(String param1, String param2) {
        PreguntasInfinitasFragment fragment = new PreguntasInfinitasFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_preguntas_infinitas, container, false);

        pregunta = (TextView) v.findViewById(R.id.pregunta);
        rA = (Button) v.findViewById(R.id.rA);
        rB = (Button) v.findViewById(R.id.rB);
        rC = (Button) v.findViewById(R.id.rC);
        rD = (Button) v.findViewById(R.id.rD);
        siguiente = (Button) v.findViewById(R.id.siguiente);

        contaBuenas = 0;
        contaMalas = 0;
        contaTotal = 0;

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        traePreguntas();

        rA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rA);
            }
        });

        rB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rB);
            }
        });

        rC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rC);
            }
        });

        rD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(rD);
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeoSiguiente();
            }
        });

        return v;
    }

    public void traePreguntas(){
        databaseReference.child(nodoPregunta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayPregunta.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Pregunta preguntaa = snapshot.getValue(Pregunta.class);
                        arrayPregunta.add(preguntaa);
                    }
                    cargaPregunta(0);
                    Log.w("PreguntasInfinitasF.", "Termine de agregar");
                 }else
                    Toast.makeText(getContext(), "No hay preguntas en la base", Toast.LENGTH_SHORT).show();
                    Log.w("Preguntas", "No hay preguntas en la base");
             }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cargaPregunta(int index){
        pregunta.setText(arrayPregunta.get(index).getPregunta());
        rA.setText(arrayPregunta.get(index).getrA());
        rB.setText(arrayPregunta.get(index).getrB());
        rC.setText(arrayPregunta.get(index).getrC());
        rD.setText(arrayPregunta.get(index).getrD());
        solucion = arrayPregunta.get(index).getSolucion();
    }

    public void clickeo(View v){
        if(v.getTag().toString().equals(solucion)){
            //Si la contesto bien
            contaBuenas = contaBuenas + 1;
            contaTotal = contaTotal + 1;
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else {
            //Si la contesto mal
            contaMalas = contaMalas + 1;
            contaTotal = contaTotal + 1;
            v.setBackgroundColor(getResources().getColor(R.color.negro));
            if (solucion.equals("A")) {
                rA.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("B")) {
                rB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("C")) {
                rC.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("D")) {
                rD.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
        siguiente.setEnabled(true);
    }

    public void clickeoSiguiente(){
        if(arrayPregunta.size()>contaTotal){
            limpiaColor();
            //Enviando nueva pregunta
            cargaPregunta(contaTotal);
            siguiente.setEnabled(false);
        }else{
            Toast.makeText(getContext(), "Ya has contestado todas las preguntas", Toast.LENGTH_SHORT).show();
            //AQUI
        }
    }

    public void limpiaColor(){
        rA.setBackgroundColor(getResources().getColor(R.color.blanco));
        rB.setBackgroundColor(getResources().getColor(R.color.blanco));
        rC.setBackgroundColor(getResources().getColor(R.color.blanco));
        rD.setBackgroundColor(getResources().getColor(R.color.blanco));
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
}
