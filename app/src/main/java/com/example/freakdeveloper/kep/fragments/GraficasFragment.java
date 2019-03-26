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

public class GraficasFragment extends Fragment
{
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
    private int [] ColoresAE = new int[] {CA , CE};


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
    private int[] errores= new int[11];

    private String[] materias=new String[]{"Alge" , "Bio" , " CInteg" , "ComTex" , "Fisica" ,"GeoAn" , "GeoYTri" , "Proba" , "ProEsc", "Quimi" , "RazMat"};

    private int [] AE = new int[2];
    ///Tot 1=== Aciertos Total Tot 2=== Errores

    private String [] TextAE = new String[]{"Aciertos" , "Erorres"};
    private BarChart barChart;
    private PieChart pieChart;
    private BarChart barChartE;
    private int [] aciertosPor = new int[11];
    private int[] erroresPor = new int[11];
    public GraficasFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graficas, container, false);

        this.barChart = (BarChart)view.findViewById(R.id.barChar);
        this.pieChart= (PieChart)view.findViewById(R.id.pieChart);
        this.barChartE = (BarChart) view.findViewById(R.id.barCharError);
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
                            aciertos[1]=respuestas.getBiologia();
                            totales[1]=respuestas.getTotalBiologia();
                            aciertos[2]=respuestas.getCalculoDiferencialeIntegral();
                            totales[2]=respuestas.getTotalCalculoDiferencialeIntegral();
                            aciertos[3]=respuestas.getComprensiondeTextos();
                            totales[3]=respuestas.getTotalComprensiondeTextos();
                            aciertos[4]=respuestas.getFisica();
                            totales[4]=respuestas.getTotalFisica();
                            aciertos[5]=respuestas.getGeometriaAnalitica();
                            totales[5]=respuestas.getTotalGeometriaAnalitica();
                            aciertos[6]=respuestas.getGeometriayTrigonometria();
                            totales[6]=respuestas.getTotalGeometriayTrigonometria();
                            aciertos[7]=respuestas.getProbabilidadyEstadistica();
                            totales[7]=respuestas.getTotalProbabilidadyEstadistica();
                            aciertos[8]=respuestas.getProduccionEscrita();
                            totales[8]=respuestas.getTotalProduccionEscrita();
                            aciertos[9]=respuestas.getQuimica();
                            totales[9]=respuestas.getTotalQuimica();
                            aciertos[10]=respuestas.getRazonamientoMatematico();
                            totales[10]=respuestas.getTotalRazonamientoMatematico();

                            break;
                        }
                    }


                        Errores();
                        Aciertos_Errores();
                        Porcentaje();
                        //graficas
                        createChartBarra();
                        createPieChart();

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

    private Chart getSameChartPie(Chart chart , String Des , int TextC , int Back , int Time)
    {
        chart.getDescription().setText(Des);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(Back);
        chart.animateY(Time);

        legendPie(chart);
        return chart;
    }

    private void legend(Chart chart)
    {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);

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

    private void legendPie(Chart chart)
    {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList <LegendEntry> entries = new ArrayList<> ();
        for(int i=0; i<AE.length ; i++)
        {
            LegendEntry entry = new LegendEntry();
            entry.formColor= ColoresAE[i];
            entry.label = TextAE[i];
            entries.add(entry);
        }

        legend.setCustom(entries);
    }

    private ArrayList<BarEntry>getBarEntries()
    {
        ArrayList <BarEntry> entries = new ArrayList<> ();
        for(int i=0; i<aciertos.length ; i++)
        {
            entries.add(new BarEntry(i , aciertosPor[i]));
        }
        return entries;
    }

    private ArrayList<BarEntry>getBarEntriesErrores()
    {
        ArrayList <BarEntry> entries = new ArrayList<> ();
        for(int i=0; i<aciertos.length ; i++)
        {
            entries.add(new BarEntry(i , erroresPor[i]));
        }
        return entries;
    }

    private ArrayList<PieEntry> getPieEntries ()
    {
        ArrayList <PieEntry> entries = new ArrayList<> ();
        for(int i=0; i<AE.length ; i++)
        {
            entries.add(new PieEntry( AE[i]));
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

    private void createChartBarra()
    {
        barChart = (BarChart) getSameChart(barChart, "" , Color.BLACK , Color.WHITE , 5000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setData(getBarData());
        barChart.getLegend().setEnabled(false);
        axisX(barChart.getXAxis());
        axisLeft(barChart.getAxisLeft());
        axisRight(barChart.getAxisRight());

        barChartE = (BarChart) getSameChart(barChartE, "" , Color.BLACK , Color.WHITE , 5000);
        barChartE.setDrawGridBackground(true);
        barChartE.setDrawBarShadow(false);
        barChartE.setData(getBarDataE());
        barChartE.getLegend().setEnabled(false);
        axisX(barChartE.getXAxis());
        axisLeft(barChartE.getAxisLeft());
        axisRight(barChartE.getAxisRight());
    }

    private void createPieChart()
    {
        pieChart = (PieChart) getSameChartPie(pieChart, "T O T A L E S" , Color.BLACK , Color.WHITE , 5000);
        pieChart.setHoleRadius(50);
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

    private DataSet getDataPie (DataSet dataSet)
    {
        dataSet.setColors(this.ColoresAE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData()
    {
        BarDataSet barDataSet= (BarDataSet) getData(new BarDataSet(getBarEntries() , ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.7f);
        return barData;
    }

    private BarData getBarDataE()
    {
        BarDataSet barDataSet= (BarDataSet) getData(new BarDataSet(getBarEntriesErrores() , ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.7f);
        return barData;
    }

    private PieData getPieData()
    {
        PieDataSet pieDataSet = (PieDataSet) getDataPie(new PieDataSet(getPieEntries() , ""));
        pieDataSet.setSliceSpace(1);
        pieDataSet.setValueFormatter(new PercentFormatter());

        return new PieData(pieDataSet);
    }

    private void Aciertos_Errores()
    {

        for(int i=0; i<materias.length ; i++)
        {
            AE [0] = AE[0] + aciertos[i];
            AE [1] = AE[1] + (totales[i] - aciertos [i]);
        }
    }

    private void Errores()
    {

        for(int i=0; i<materias.length ; i++)
        {
            errores[i] = totales[i] - aciertos [i];
        }
    }

    private void Porcentaje()
    {
        int TotalP = 0;
        for(int i=0; i<materias.length ; i++)
        {
            TotalP = TotalP + totales [i] ;
        }

        for(int i=0; i<materias.length ; i++)
        {
            aciertosPor [i] = (aciertos [i]*100)/totales[i];
            erroresPor [i] = ((totales[i] - aciertos[i]))*100/totales[i];
        }


    }
}
