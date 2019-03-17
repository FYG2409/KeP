package com.example.freakdeveloper.kep;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.freakdeveloper.kep.model.Duelo;
import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Preguntas extends AppCompatActivity {

    private String materiaSeleccionada;

    //MIS VARIABLES

    private TextView txtPregunta;
    private Button btnRa, btnRb, btnRc, btnRd, siguiente;
    private String solucion;
    private int contaBuenas, contaMalas, contaTotal;

    private ArrayList<Pregunta> arrayPregunta = new ArrayList<>();
    private  static final String nodoPregunta="Preguntas";
    private  static final String nodoDuelos="Duelos";

    //PARA TIMER
    private TextView countdownText;

    private CountDownTimer countDownTimer;
    private long tiempoEnMilisegundos = 10000; //Son los minutos que queremos en milisegundos

    private String codigoDuelo, tipoPersona, email;

    //PARA FIREBASE
    private DatabaseReference databaseReference;

    //PARA IMAGENES
    private ImageView imgRa, imgRb, imgRc, imgRd, imgPregunta;
    private StorageReference storageRef;
    private static final String nodoPreguntasImg="Preguntas/";
    private static final String nodoRespuestasImg="Respuestas/";
    private ProgressDialog progressDialog;

    private LinearLayout resA, resB, resC, resD;

    //POP-UP
    Dialog miVentana;
    String msj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        recuperaDatosIntent();

        //-----PARA IMAGENES----
        storageRef = FirebaseStorage.getInstance().getReference();
        imgRa = (ImageView) findViewById(R.id.imgRa);
        imgRb = (ImageView) findViewById(R.id.imgRb);
        imgRc = (ImageView) findViewById(R.id.imgRc);
        imgRd = (ImageView) findViewById(R.id.imgRd);
        imgPregunta = (ImageView) findViewById(R.id.imgPregunta);
        //----------------------

        txtPregunta = (TextView) findViewById(R.id.txtPregunta);
        btnRa = (Button) findViewById(R.id.btnRa);
        btnRb = (Button) findViewById(R.id.btnRb);
        btnRc = (Button) findViewById(R.id.btnRc);
        btnRd = (Button) findViewById(R.id.btnRd);
        siguiente = (Button) findViewById(R.id.siguiente);

        resA = (LinearLayout) findViewById(R.id.resA);
        resB = (LinearLayout) findViewById(R.id.resB);
        resC = (LinearLayout) findViewById(R.id.resC);
        resD = (LinearLayout) findViewById(R.id.resD);

        contaBuenas = 0;
        contaMalas = 0;
        contaTotal = 0;

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //PARA POP-UP
        miVentana = new Dialog(this);

        traePreguntas();
    }

    public void recuperaDatosIntent(){
        Bundle extras = getIntent().getExtras();
        materiaSeleccionada = extras.getString("materia");
        email = extras.getString("email");

        //EXTRAS CUANDO VENGO DE DueloFragment
        tipoPersona = extras.getString("tipoPersona");
        codigoDuelo = extras.getString("codigoDuelo");
        if(materiaSeleccionada.equals("todas")){
            ponTimer();
        }
    }

    public void traePreguntas(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Trayendo datos...");
        progressDialog.setMessage("Trayendo preguntas de firebase");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.child(nodoPregunta).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayPregunta.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Pregunta preguntaa = snapshot.getValue(Pregunta.class);
                        if(preguntaa.getMateria().equalsIgnoreCase(materiaSeleccionada)){
                            arrayPregunta.add(preguntaa);
                        }else
                        if(materiaSeleccionada.equals("todas")){
                            arrayPregunta.add(preguntaa);
                        }else
                            Log.w("Preguntas", "No hay preguntas de esta materia en la base");
                    }
                    cargaPregunta(0);
                    Log.w("Preguntas", "Termine de agregar");
                    progressDialog.dismiss();
                }else
                    Toast.makeText(Preguntas.this, "No hay preguntas en la base", Toast.LENGTH_SHORT).show();
                Log.w("Preguntas", "No hay preguntas en la base");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cargaPregunta(int index){
        txtPregunta.setText(arrayPregunta.get(index).getPregunta());
        btnRa.setText(arrayPregunta.get(index).getrA());
        btnRb.setText(arrayPregunta.get(index).getrB());
        btnRc.setText(arrayPregunta.get(index).getrC());
        btnRd.setText(arrayPregunta.get(index).getrD());
        solucion = arrayPregunta.get(index).getSolucion();

        if(arrayPregunta.get(index).getPreguntaImg() != null){
            traeImagen(arrayPregunta.get(index).getPreguntaImg(), 1, nodoPreguntasImg);
        }
        if(arrayPregunta.get(index).getrAImg() != null){
            traeImagen(arrayPregunta.get(index).getrAImg(), 2, nodoRespuestasImg);
        }
        if(arrayPregunta.get(index).getrBImg() != null){
            traeImagen(arrayPregunta.get(index).getrBImg(), 3, nodoRespuestasImg);
        }
        if(arrayPregunta.get(index).getrCImg() != null){
            traeImagen(arrayPregunta.get(index).getrCImg(), 4, nodoRespuestasImg);
        }
        if(arrayPregunta.get(index).getrDImg() != null){
            traeImagen(arrayPregunta.get(index).getrDImg(), 5, nodoRespuestasImg);
        }
    }

    public void traeImagen(String nombre, final int regCode, String nodo){
        Task<Uri> uriTask = storageRef.child(nodo).child(nombre).getDownloadUrl();
        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView img = null;
                if(regCode==1){
                    img = imgPregunta;
                }else
                if(regCode==2){
                    img = imgRa;
                }else
                if(regCode==3){
                    img = imgRb;
                }else
                if(regCode==4){
                    img = imgRc;
                }else
                if(regCode==5){
                    img = imgRd;
                }

                Glide.with(Preguntas.this)
                        .load(uri)
                        .into(img);

                Log.w("Preguntas", "Imagen traida correctamente");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Preguntas", "Error al traer imagen: "+ e.toString());
            }
        });
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
                resA.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("B")) {
                resB.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("C")) {
                resC.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else if (solucion.equals("D")) {
                resD.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }
        siguiente.setEnabled(true);
    }

    public void clickeoSiguiente(View v){
        if(arrayPregunta.size()>contaTotal){
            limpiaCampos();
            //Enviando nueva pregunta
            cargaPregunta(contaTotal);
            siguiente.setEnabled(false);
        }else{
            Toast.makeText(this, "Ya has contestado todas las preguntas", Toast.LENGTH_SHORT).show();
            onStop();
        }
    }

    public void limpiaCampos(){
        //LIMPIANDO COLOR
        resA.setBackgroundColor(getResources().getColor(R.color.blanco));
        resB.setBackgroundColor(getResources().getColor(R.color.blanco));
        resC.setBackgroundColor(getResources().getColor(R.color.blanco));
        resD.setBackgroundColor(getResources().getColor(R.color.blanco));
        //LIMPIANDO IMAGENES
        imgPregunta.setImageDrawable(null);
        imgRa.setImageDrawable(null);
        imgRb.setImageDrawable(null);
        imgRc.setImageDrawable(null);
        imgRd.setImageDrawable(null);
        //LIMPIANDO TEXTO BOTONES
        btnRa.setText("");
        btnRb.setText("");
        btnRc.setText("");
        btnRd.setText("");
        //LIMPIANDO PREGUNTA
        txtPregunta.setText("");
    }

    public void muestraPopUp(View view){
        TextView txtClose, txtBuenas, txtMalas;
        miVentana.setContentView(R.layout.my_pop_up);
        txtClose = (TextView) miVentana.findViewById(R.id.txtclose);
        txtBuenas = (TextView) miVentana.findViewById(R.id.txtBuenas);
        txtMalas = (TextView) miVentana.findViewById(R.id.txtMalas);
        txtBuenas.setText(Integer.toString(contaBuenas));
        txtMalas.setText(Integer.toString(contaMalas));
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miVentana.dismiss();
                finish();
            }
        });
        miVentana.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        miVentana.show();
    }



    //-------PARA TIMER------------

    public void ponTimer(){
        countdownText = (TextView) findViewById(R.id.countdown_text);
        countdownText.setVisibility(View.VISIBLE);
        iniciaTimer();
    }

    public void iniciaTimer(){
        countDownTimer = new CountDownTimer(tiempoEnMilisegundos, 1000) {
            @Override
            public void onTick(long lon) {
                tiempoEnMilisegundos = lon;
                updateTimer();
            }
            @Override
            public void onFinish() {
                //AQUI TERMINA EL TIEMPO
                if(tipoPersona.equals("Uno")){
                    databaseReference.child(nodoDuelos).child(codigoDuelo).child("totalBuenasUno").setValue(contaBuenas);
                }else
                    if(tipoPersona.equals("Dos")){
                        databaseReference.child(nodoDuelos).child(codigoDuelo).child("totalBuenasDos").setValue(contaBuenas);
                    }else
                        Log.w("Preguntas", "Tipo de persona desconocido");

                verGanador();
            }
        }.start();
     }

    public void verGanador(){
        final TextView txtClose, txtBuenas, txtMalas, txtMensaje;
        miVentana.setContentView(R.layout.my_pop_up);
        txtClose = (TextView) miVentana.findViewById(R.id.txtclose);
        txtBuenas = (TextView) miVentana.findViewById(R.id.txtBuenas);
        txtMalas = (TextView) miVentana.findViewById(R.id.txtMalas);
        txtMensaje = (TextView) miVentana.findViewById(R.id.txtMensaje);
        miVentana.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtClose.setVisibility(View.INVISIBLE);
        miVentana.show();


        //-----TRAYENDO GANADOR Y PERDEDOR
        databaseReference.child(nodoDuelos).child(codigoDuelo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Si existe el codigo
                    Duelo duelo = dataSnapshot.getValue(Duelo.class);
                    Long contaBuenasUno = duelo.getTotalBuenasUno();
                    Long contaBuenasDos = duelo.getTotalBuenasDos();

                    String contaBuenasUnoTxt = String.valueOf(contaBuenasUno);
                    String contaBuenasDosTxt = String.valueOf(contaBuenasDos);


                    if(contaBuenasUnoTxt.equals("null")||contaBuenasDosTxt.equals("null")){
                        msj="Espera...";
                    }else{
                        if(contaBuenasUno>contaBuenasDos){
                            if(tipoPersona.equals("Uno")){
                                msj="Ganaste";
                            }else
                                if(tipoPersona.equals("Dos")){
                                    msj="Perdiste";
                                }
                        }else
                        if(contaBuenasDos>contaBuenasUno){
                            if(tipoPersona.equals("Dos")){
                                msj="Ganaste";
                            }else
                            if(tipoPersona.equals("Uno")){
                                msj="Perdiste";
                            }
                        }else
                        if(contaBuenasDos == contaBuenasUno){
                            msj="Empate";
                        }

                        txtMensaje.setText(msj);
                        txtBuenas.setText(Integer.toString(contaBuenas));
                        txtMalas.setText(Integer.toString(contaMalas));

                        txtClose.setVisibility(View.VISIBLE);
                        txtClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                miVentana.dismiss();
                                databaseReference.child(nodoDuelos).child(codigoDuelo).removeValue();
                                finish();
                            }
                        });

                    }
                    txtMensaje.setText(msj);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //--------------------------------
     }

    public void updateTimer(){
        int minutos = (int) tiempoEnMilisegundos / 60000;
        int segundos = (int) tiempoEnMilisegundos % 60000 / 1000;

        String timeLeftText;

        timeLeftText = ""+minutos;
        timeLeftText += ":";
        if(segundos<10) timeLeftText += 0;
        timeLeftText += segundos;
        countdownText.setText(timeLeftText);
    }

    //-----------------------------


}
