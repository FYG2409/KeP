package com.example.freakdeveloper.kep.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.freakdeveloper.kep.Preguntas;
import com.example.freakdeveloper.kep.R;


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
    private LinearLayout razMatematico, algebra, geoTrigo,geoAnalitica, calDifIntegral, probaEstadistica, prodEscrita, comTextos, biologia, quimica, fisica;


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

        razMatematico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(razMatematico);
            }
        });

        algebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(algebra);
            }
        });

        geoTrigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(geoTrigo);
            }
        });

        geoAnalitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(geoAnalitica);
            }
        });

        calDifIntegral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(calDifIntegral);
            }
        });

        probaEstadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(probaEstadistica);
            }
        });

        prodEscrita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(prodEscrita);
            }
        });

        comTextos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(comTextos);
            }
        });

        biologia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(biologia);
            }
        });

        quimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(quimica);
            }
        });

        fisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeo(fisica);
            }
        });

        return v;
    }

    public void clickeo(View view){
        Intent intent = new Intent(getContext(), Preguntas.class);
        String materia = view.getTag().toString();
        intent.putExtra("materia", materia);
        startActivity(intent);
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
