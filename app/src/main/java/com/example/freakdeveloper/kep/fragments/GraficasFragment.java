package com.example.freakdeveloper.kep.fragments;

import android.os.Bundle;
import android.graphics.Color;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freakdeveloper.kep.R;
import com.example.freakdeveloper.kep.model.Respuestas;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraficasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraficasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraficasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Colores
    private int CA= Color.rgb(167, 254, 58); //(aciertos)
    private int CE= Color.rgb(255, 97 , 97); //(errores);

    private int C1= Color.rgb(97  , 185 , 255);
    private int C2= Color.rgb(255 , 167 , 97);
    private int C3= Color.rgb(252 , 166 , 246);
    private int C4= Color.rgb(246 , 255 , 131);
    private int C5= Color.rgb(166 , 166 , 252);
    private int C6= Color.rgb(166 , 252 , 197);
    private int C7= Color.rgb(188 , 106 , 106);
    private int C8= Color.rgb(2   , 71  , 117);
    private int C9= Color.rgb(181 , 103 , 255);
    private int C10= Color.rgb(217, 176 , 80);
    private int C11= Color.rgb(138, 220 , 201);

    private int [] Colores =  new int [] {C1, C2 , C3 , C4 , C5 , C6, C7, C8 , C9 , C10, C11};


    //FIREBASE

    DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private  static final String nodoPersona="Personas";
    private  static final String nodoRespuestas="Respuestas";

    //VARIABLES
    private String Email;
    private int[] totales=new int[11];
    private int[] aciertos=new int[11];
    private String[] materias=new String[11];

    private BarChart barChart;
    private PieChart pieChart;
    private String [] Materias= new String[]{"Matematicas", "Español" , "Fisica" , "Quimica"};
    private int [] Aciertos = new int[] {15, 2, 30, 20};
    //private int [] Colores =  new int [] {Color.BLACK , Color.RED , Color.BLUE , Color.MAGENTA};

    public GraficasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraficasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraficasFragment newInstance(String param1, String param2) {
        GraficasFragment fragment = new GraficasFragment();
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
        View view = inflater.inflate(R.layout.fragment_graficas, container, false);

        this.barChart = (BarChart)view.findViewById(R.id.barChar);
        this.pieChart= (PieChart)view.findViewById(R.id.pieChart);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Trae Respuestas
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(nodoRespuestas).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Respuestas respuestas = snapshot.getValue(Respuestas.class);

                        String IDres=snapshot.getKey();
                        if(IDres.equals(user.getUid()))
                        {
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
                    }
                    //graficas
                    createChart();
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Chart getSameChart(Chart chart , String Des , int TextC , int Back , int Time)
    {
        chart.getDescription().setText(Des);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(Back);
        chart.animateY(Time);

        legend(chart);
        return chart;
    }

    private void legend(Chart chart)
    {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList <LegendEntry> entries = new ArrayList<> ();
        for(int i=0; i<aciertos.length ; i++)
        {
            LegendEntry entry = new LegendEntry();
            entry.formColor= Colores[i];
            entry.label = materias[i];
            entries.add(entry);
        }

        legend.setCustom(entries);
    }

    private ArrayList<BarEntry>getBarEntries()
    {
        ArrayList <BarEntry> entries = new ArrayList<> ();
        for(int i=0; i<aciertos.length ; i++)
        {
            entries.add(new BarEntry(i , aciertos[i]));
        }
        return entries;
    }

    private ArrayList<PieEntry> getPieEntries ()
    {
        ArrayList <PieEntry> entries = new ArrayList<> ();
        for(int i=0; i<materias.length ; i++)
        {
            entries.add(new PieEntry( aciertos[i]));
        }
        return entries;
    }

    private void axisX(XAxis axis)
    {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(materias));
    }

    private void axisLeft(YAxis axis)
    {
        axis.setSpaceBottom(60);
        axis.setAxisMinimum(0);
    }

    private void axisRight(YAxis axis)
    {
        axis.setEnabled(false);
    }

    private void createChart()
    {
        barChart = (BarChart) getSameChart(barChart, "ACIERTOS" , Color.BLACK , Color.WHITE , 5000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setData(getBarData());
        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());

        pieChart = (PieChart) getSameChart(pieChart, "ACIERTOS" , Color.BLACK , Color.WHITE , 5000);
        pieChart.setHoleRadius(15);
        pieChart.setData(getPieData());
        pieChart.setTransparentCircleRadius(6);
        pieChart.invalidate();
    }

    private DataSet getData (DataSet dataSet)
    {
        dataSet.setColors(this.Colores);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData()
    {
        BarDataSet barDataSet= (BarDataSet) getData(new BarDataSet(getBarEntries() , ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.25f);
        return barData;
    }

    private PieData getPieData()
    {
        PieDataSet pieDataSet = (PieDataSet) getData(new PieDataSet(getPieEntries() , ""));
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueFormatter(new PercentFormatter());

        return new PieData(pieDataSet);
    }
}
