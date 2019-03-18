package com.example.freakdeveloper.kep.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.freakdeveloper.kep.Preguntas;
import com.example.freakdeveloper.kep.R;
import com.example.freakdeveloper.kep.RegistroPregunta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PreguntasPorMateriaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //MIS VARIABLES
    private int totalPreguntas, conta;
    int[] totales = new int[11];
    private Boolean todasMaterias = false;

    private LinearLayout razMatematico, algebra, geoTrigo,geoAnalitica, calDifIntegral, probaEstadistica, prodEscrita, comTextos, biologia, quimica, fisica, infinito;
    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private  static final String nodoPregunta="Preguntas";

    private String materia;

    public PreguntasPorMateriaFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PreguntasPorMateriaFragment newInstance(String param1, String param2) {
        PreguntasPorMateriaFragment fragment = new PreguntasPorMateriaFragment();
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
        View v = inflater.inflate(R.layout.fragment_preguntas_por_materia, container, false);
        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        infinito = (LinearLayout) v.findViewById(R.id.infinito);
        razMatematico = (LinearLayout) v.findViewById(R.id.razMatematico);
        algebra = (LinearLayout) v.findViewById(R.id.algebra);
        geoTrigo = (LinearLayout) v.findViewById(R.id.geoTrigo);
        geoAnalitica = (LinearLayout) v.findViewById(R.id.geoAnalitica);
        calDifIntegral = (LinearLayout) v.findViewById(R.id.calDifIntegral);
        probaEstadistica = (LinearLayout) v.findViewById(R.id.probaEstadistica);
        prodEscrita = (LinearLayout) v.findViewById(R.id.prodEscrita);
        comTextos = (LinearLayout) v.findViewById(R.id.compreTextos);
        biologia = (LinearLayout) v.findViewById(R.id.biologia);
        quimica = (LinearLayout) v.findViewById(R.id.quimica);
        fisica = (LinearLayout) v.findViewById(R.id.fisica);

        conta = 0;

        infinito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaInfinito(infinito);
            }
        });

        razMatematico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(razMatematico);
            }
        });

        algebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(algebra);
            }
        });

        geoTrigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(geoTrigo);
            }
        });

        geoAnalitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(geoAnalitica);
            }
        });

        calDifIntegral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(calDifIntegral);
            }
        });

        probaEstadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(probaEstadistica);
            }
        });

        prodEscrita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(prodEscrita);
            }
        });

        comTextos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(comTextos);
            }
        });

        biologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(biologia);
            }
        });

        quimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(quimica);
            }
        });

        fisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaExistenPreguntas(fisica);
            }
        });

        return v;
    }

    public void cambiaActivity(int totalHijos){
        Intent intent = new Intent(getContext(), Preguntas.class);
        intent.putExtra("materia", materia);
        intent.putExtra("totalHijos", totalHijos);
        startActivity(intent);
    }

    public void valida(String materia){
        databaseReference.child(nodoPregunta).child(materia).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    totalPreguntas = (int) dataSnapshot.getChildrenCount();

                }else{
                    //Si aun no hay registros para esa materia
                    totalPreguntas = 0;
                }
                if(todasMaterias){
                    totales[conta] = totalPreguntas;
                    conta = conta+1;
                    if(conta==11){
                        Boolean primera = true;
                        totalPreguntas = 0;
                        for(int i = 0; i<11; i++){
                            if(primera){
                                totalPreguntas = totales[i];
                                primera = false;
                            }else
                                if(totalPreguntas>totales[i]){
                                    totalPreguntas = totales[i];
                                }
                        }
                        Log.w("FEIK", "Menor "+Integer.toString(totalPreguntas));
                        if(totalPreguntas==0){
                            Toast.makeText(getContext(), "Lo sentimos aun no tenemos preguntas para todas las materias", Toast.LENGTH_SHORT).show();
                        }else{
                            cambiaActivity(totalPreguntas);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validaExistenPreguntas(View view){
        materia = view.getTag().toString();
        todasMaterias=false;
        valida(materia);
        if(totalPreguntas==0){
            //Si no existieron preguntas
            Toast.makeText(getContext(), "Lo sentimos aun no tenemos preguntas para esa matetria", Toast.LENGTH_SHORT).show();
        }else{
            //Si existieron preguntas
            cambiaActivity(totalPreguntas);
        }
    }

    public void validaInfinito(View view) {
        materia = view.getTag().toString();
        todasMaterias=true;
        valida("Razonamiento Matematico");
        valida("Algebra");
        valida("Geometria y Trigonometria");
        valida("Geometria Analitica");
        valida("Calculo Diferencial e Integral");
        valida("Probabilidad y Estadistica");
        valida("Produccion Escrita");
        valida("Comprension de Textos");
        valida("Biologia");
        valida("Quimica");
        valida("Fisica");
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
