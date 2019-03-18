package com.example.freakdeveloper.kep.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
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

        private Button btnCreaCodigo, btnTengoCodigo, btnJugar, btnElMejor, btnContraTiempo;
        private TextView tvEsperandoOponente;
        private EditText edtIngresaCodigo;
        private String NickName, email, codigoDuelo, tipoDuelo;
        private int conta, totalPreguntas, numeroAleatorio;
        int[] totales = new int[11];

        //PARA FIREBASE
        private DatabaseReference databaseReference;
        private FirebaseAuth.AuthStateListener authStateListener;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private  static final String nodoPregunta="Preguntas";

        private  static final String nodoDuelos="Duelos";
        private  static final String nodoPersona="Personas";


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

            //PARA FIREBASE
            databaseReference = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();

            btnElMejor = (Button) view.findViewById(R.id.btnElMejor);
            btnContraTiempo = (Button) view.findViewById(R.id.btnContraTiempo);
            btnCreaCodigo = (Button) view.findViewById(R.id.btnCreaCodigo);
            btnTengoCodigo = (Button) view.findViewById(R.id.btnTengoCodigo);
            btnJugar = (Button) view.findViewById(R.id.btnJugar);
            edtIngresaCodigo = (EditText) view.findViewById(R.id.edtIngresaCodigo);
            tvEsperandoOponente = (TextView) view.findViewById(R.id.tvEsperandoOponente);

            //-----TRAYENDO PERSONA----
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
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //-------------

            btnTengoCodigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    metTengoCodigo();
                }
            });

            btnCreaCodigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    metCreaCodigo();
                }
            });

        return view;
    }

    public void metCreaCodigo(){
        btnCreaCodigo.setVisibility(View.GONE);
        btnTengoCodigo.setVisibility(View.GONE);
        btnJugar.setVisibility(View.GONE);
        btnElMejor.setVisibility(View.VISIBLE);
        btnContraTiempo.setVisibility(View.VISIBLE);
        //btnJugar.setVisibility(View.VISIBLE);

        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text",  codigoDuelo);
        Toast.makeText(getContext(), "Codigo copiado a portapapeles", Toast.LENGTH_SHORT).show();
        clipboard.setPrimaryClip(clip);

        btnElMejor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEsperandoOponente.setVisibility(View.VISIBLE);
                tipoDuelo = "elMejor";
                validandoCodigoPerUno();
            }
        });

        btnContraTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEsperandoOponente.setVisibility(View.VISIBLE);
                tipoDuelo = "contraTiempo";
                numAleatorio();
            }
        });
        /*
        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvEsperandoOponente.setVisibility(View.VISIBLE);
                validandoCodigoPerUno();
            }
        });*/

    }

    public void metTengoCodigo(){
        btnCreaCodigo.setVisibility(View.GONE);
        btnTengoCodigo.setVisibility(View.GONE);
        btnJugar.setVisibility(View.VISIBLE);
        edtIngresaCodigo.setVisibility(View.VISIBLE);

        btnJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoIngresado = edtIngresaCodigo.getText().toString();
                if(codigoIngresado.isEmpty()){
                    Toast.makeText(getContext(), "Ingresa un codigo", Toast.LENGTH_SHORT).show();
                }else{
                    validandoCodigoPerDos(codigoIngresado);
                }
            }
        });
    }

    public void validandoCodigoPerDos(String codIngresado){
        final String codigoIngresadoTxt = codIngresado;
        databaseReference.child(nodoDuelos).child(codigoIngresadoTxt).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Si existe el codigo
                    Duelo duelo = dataSnapshot.getValue(Duelo.class);
                    if(duelo.getCorreoPerDos()==null){
                        //LA SEGUNDA PERSONA ESTA DISPONIBLE
                        tipoDuelo = duelo.getTipoDuelo();
                        numeroAleatorio = (duelo.getNumAleatorio()).intValue();
                        totalPreguntas = (duelo.getTotalPreguntas()).intValue();
                        iniciaDueloPerDos(codigoIngresadoTxt);
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

    public void validandoCodigoPerUno(){
        conta=0;
        Duelo duelo = new Duelo(email, null, null, null, (long)numeroAleatorio, tipoDuelo, (long)totalPreguntas);
        databaseReference.child(nodoDuelos).child(codigoDuelo).setValue(duelo);
        Log.w("FEIK", "Duelo creado");

        databaseReference.child(nodoDuelos).child(codigoDuelo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Si existe el codigo
                    Duelo duelo = dataSnapshot.getValue(Duelo.class);
                    if(duelo.getCorreoPerDos()==null){
                        //LA SEGUNDA PERSONA ESTA DISPONIBLE
                        tvEsperandoOponente.setVisibility(View.VISIBLE);
                    }else{
                        conta = conta + 1;
                        if(conta==1){
                            //LA SEGUNDA PERSONA NO ESTA DISPONIBLE
                            reiniciaVistas();
                            Intent intent = new Intent(getContext(), Preguntas.class);
                            intent.putExtra("materia", "todas");
                            intent.putExtra("codigoDuelo",codigoDuelo);
                            intent.putExtra("tipoPersona", "Uno");
                            intent.putExtra("email", email);
                            intent.putExtra("tipoDuelo", tipoDuelo);
                            intent.putExtra("numInicio", numeroAleatorio);
                            intent.putExtra("totalHijos", totalPreguntas);
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

    public void iniciaDueloPerDos(String codigoIngresadoTxt){
        databaseReference.child(nodoDuelos).child(codigoIngresadoTxt).child("correoPerDos").setValue(email);
        reiniciaVistas();
        Intent intent = new Intent(getContext(), Preguntas.class);
        intent.putExtra("materia", "todas");
        intent.putExtra("codigoDuelo", codigoIngresadoTxt);
        intent.putExtra("tipoPersona", "Dos");
        intent.putExtra("tipoDuelo", tipoDuelo);
        intent.putExtra("numInicio", numeroAleatorio);
        intent.putExtra("totalHijos", totalPreguntas);
        startActivity(intent);
    }

    public void reiniciaVistas(){
        btnCreaCodigo.setVisibility(View.VISIBLE);
        btnTengoCodigo.setVisibility(View.VISIBLE);
        edtIngresaCodigo.setVisibility(View.GONE);
        btnJugar.setVisibility(View.GONE);
        tvEsperandoOponente.setVisibility(View.GONE);
        btnElMejor.setVisibility(View.GONE);
        btnContraTiempo.setVisibility(View.GONE);
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
                        //GENERANDO NUM ALEATORIO
                        numeroAleatorio = (int) (Math.random() * totalPreguntas) + 1;
                        validandoCodigoPerUno();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void numAleatorio(){
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


}
