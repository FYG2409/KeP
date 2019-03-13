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
    private CardView fisica;


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

        fisica = (CardView) v.findViewById(R.id.fisica);

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
