package com.example.freakdeveloper.kep.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.freakdeveloper.kep.NavigationDrawer;
import com.example.freakdeveloper.kep.R;
import com.example.freakdeveloper.kep.model.Persona;
import com.example.freakdeveloper.kep.model.Respuestas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class PerfilFragment extends Fragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //FIREBASE

    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private  static final String nodoPersona="Personas";
    private  static final String nodoRespuestas="Respuestas";

    //VARIABLES
    private String Email;
    private int[] totales=new int[11];
    private int[] aciertos=new int[11];
    private String[] materias=new String[11];


    //TRAER DATOS
    private String NickName="";
    private String Correo="";
    private String EActual="";
    private String EIngresar="";


    //VISTAS
    private TextView Usu;
    private TextView Cor;
    private TextView EA;
    private TextView EI;
    private TextView Ran;
    private Button btnAjustes;
    private Button btnEliminar;
    private ListView ListaA,ListaT,ListaM;

    public PerfilFragment()
    {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(String param1, String param2)
    {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        //Trae NickName, Correo, Escuela Actual y Escuela a Ingresar
        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        btnAjustes = (Button) view.findViewById(R.id.btnAjust);
        btnEliminar = (Button) view.findViewById(R.id.btnDelete);
        Usu = (TextView) view.findViewById(R.id.NickN);
        Cor = (TextView) view.findViewById(R.id.CorreO);
        EA = (TextView) view.findViewById(R.id.E_A);
        EI = (TextView) view.findViewById(R.id.E_I);
        Ran= (TextView) view.findViewById(R.id.RankG);
        ListaA=(ListView) view.findViewById(R.id.listA);
        ListaT=(ListView) view.findViewById(R.id.listT);
        ListaM=(ListView) view.findViewById(R.id.listM);

        //-----TRAYENDO PERSONA----
        user = FirebaseAuth.getInstance().getCurrentUser();
        Email=user.getEmail();

        databaseReference.child(nodoPersona).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Persona persona = snapshot.getValue(Persona.class);
                        String mail=persona.getEmail();
                        if(mail.equals(Email)){
                            //SE ENCONTRO LA PERSONA CON EL EMAIL INDICADO
                            NickName = persona.getNickName();
                            Correo=persona.getEmail();
                            EActual=persona.getEscActual();
                            EIngresar=persona.getEscingresar();
                            break;
                        }
                        else {

                        }
                    }
                    Usu.setText("NickName: " + NickName );
                    Cor.setText("Email: " + Correo );
                    EA.setText("Escuela Actual: " + EActual );
                    EI.setText("Escuela a Ingresar: " + EIngresar );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Trae Respuestas

        databaseReference.child(nodoRespuestas).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Respuestas respuestas = snapshot.getValue(Respuestas.class);

                        String IDres=snapshot.getKey();
                        if(IDres.equals(user.getUid())){
                            //SE ENCONTRO LA PERSONA CON EL ID INDICADO
                            aciertos[0]=respuestas.getAlgebra();
                            totales[0]=respuestas.getTotalAlgebra();
                            materias[0]="Algebra";
                            aciertos[1]=respuestas.getBiologia();
                            totales[1]=respuestas.getTotalBiologia();
                            materias[1]="Biologia";
                            aciertos[2]=respuestas.getCalculoDiferencialeIntegral();
                            totales[2]=respuestas.getTotalCalculoDiferencialeIntegral();
                            materias[2]="CalculoDiferencialeIntegral";
                            aciertos[3]=respuestas.getComprensiondeTextos();
                            totales[3]=respuestas.getTotalComprensiondeTextos();
                            materias[3]="CompresiondeTextos";
                            aciertos[4]=respuestas.getFisica();
                            totales[4]=respuestas.getTotalFisica();
                            materias[4]="Fisica";
                            aciertos[5]=respuestas.getGeometriaAnalitica();
                            totales[5]=respuestas.getTotalGeometriaAnalitica();
                            materias[5]="GeometriaAnalitica";
                            aciertos[6]=respuestas.getGeometriayTrigonometria();
                            totales[6]=respuestas.getTotalGeometriayTrigonometria();
                            materias[6]="GeometriayTrigonometria";
                            aciertos[7]=respuestas.getProbabilidadyEstadistica();
                            totales[7]=respuestas.getTotalProbabilidadyEstadistica();
                            materias[7]="ProbabilidadyEstadistica";
                            aciertos[8]=respuestas.getProduccionEscrita();
                            totales[8]=respuestas.getTotalProduccionEscrita();
                            materias[8]="ProduccionEscrita";
                            aciertos[9]=respuestas.getQuimica();
                            totales[9]=respuestas.getTotalQuimica();
                            materias[9]="Quimica";
                            aciertos[10]=respuestas.getRazonamientoMatematico();
                            totales[10]=respuestas.getTotalRazonamientoMatematico();
                            materias[10]="RazonamientoMatematico";
                            break;
                        }
                        else {

                        }
                    }
                     /*aquiiii
                    //answer.setText(": " + NickName );
                    ArrayAdapter A=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, Collections.singletonList(aciertos));
                    ListaA.setAdapter(A);
                    ArrayAdapter T=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, Collections.singletonList(totales));
                    ListaT.setAdapter(T);
                    ArrayAdapter<String> M=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, materias);
                    ListaM.setAdapter(M);
                    //hacer graficas aqui
                    */

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                ad.setCancelable(false);
                ad.setTitle("Importante");
                ad.setMessage("¿Esta seguro de querer eliminar su cuenta?");
                ad.setButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.setButton("si", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        databaseReference.child(nodoPersona).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        Persona persona = snapshot.getValue(Persona.class);
                                        String idp=snapshot.getKey();
                                        String idu=user.getUid();
                                        if(idp.equals(idu)){
                                            //SE ENCONTRO LA PERSONA CON EL ID INDICADO
                                            user.delete();
                                            databaseReference.child(nodoPersona).child(idp).removeValue();
                                            dialog.dismiss();

                                            break;
                                        }
                                        else {

                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                ad.show();
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
