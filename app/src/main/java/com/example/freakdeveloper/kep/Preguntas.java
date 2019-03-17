package com.example.freakdeveloper.kep;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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
import com.example.freakdeveloper.kep.model.Persona;
import com.example.freakdeveloper.kep.model.Pregunta;
import com.example.freakdeveloper.kep.model.Respuestas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Preguntas extends AppCompatActivity {

    private Boolean hayPreguntas;
    private int bande;

    private TextView txtPregunta;
    private Button salir;
    private TextView txtRa, txtRb, txtRc, txtRd;
    private LinearLayout resA, resB, resC, resD;

    private ImageView imgRa, imgRb, imgRc, imgRd, imgPregunta, siguiente;
    private ArrayList<Pregunta> arrayPreguntas = new ArrayList<>();
    private int contaBuenas, contaMalas, contaTotal;
    private String codigoDuelo, tipoPersona, email, materiaSeleccionada, solucion;

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private  static final String nodoPregunta="Preguntas";
    private  static final String nodoDuelos="Duelos";
    private StorageReference storageRef;
    private static final String nodoPreguntasImg="Preguntas/";
    private static final String nodoRespuestasImg="Respuestas/";

    //PARA TIMER
    private TextView countdownText;
    private CountDownTimer countDownTimer;
    private long tiempoEnMilisegundos = 10000; //Son los minutos que queremos en milisegundos

    //PARA MENSAJE CARGANDO
    private ProgressDialog progressDialog;

    //PARA POP-UP
    private Dialog miVentana;
    private String msj;

    //PARA RESPUESTAS
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private  static final String nodoPersona="Personas";
    private  static final String nodoRespuestas="Respuestas";
    private String idPersona;
    private Boolean existe;
    private int contaMateria, contaMateriaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        recuperaDatosIntent();

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //-----PARA IMAGENES----
        storageRef = FirebaseStorage.getInstance().getReference();
        imgRa = (ImageView) findViewById(R.id.imgRa);
        imgRb = (ImageView) findViewById(R.id.imgRb);
        imgRc = (ImageView) findViewById(R.id.imgRc);
        imgRd = (ImageView) findViewById(R.id.imgRd);
        imgPregunta = (ImageView) findViewById(R.id.imgPregunta);
        //----------------------

        txtPregunta = (TextView) findViewById(R.id.txtPregunta);
        txtRa = (TextView) findViewById(R.id.txtRa);
        txtRb = (TextView) findViewById(R.id.txtRb);
        txtRc = (TextView) findViewById(R.id.txtRc);
        txtRd = (TextView) findViewById(R.id.txtRd);
        siguiente = (ImageView) findViewById(R.id.siguiente);

        resA = (LinearLayout) findViewById(R.id.resA);
        resB = (LinearLayout) findViewById(R.id.resB);
        resC = (LinearLayout) findViewById(R.id.resC);
        resD = (LinearLayout) findViewById(R.id.resD);
        salir = (Button) findViewById(R.id.salir);

        bande = 0;
        contaBuenas = 0;
        contaMalas = 0;
        contaTotal = 0;

        //PARA POP-UP
        miVentana = new Dialog(this);
        limpiaCampos();
        traePreguntas();
        traePersona();
    }

    public void recuperaDatosIntent(){
        Bundle extras = getIntent().getExtras();
        materiaSeleccionada = extras.getString("materia");
        email = extras.getString("email");

        //EXTRAS CUANDO VENGO DE DueloFragment
        tipoPersona = extras.getString("tipoPersona");
        codigoDuelo = extras.getString("codigoDuelo");
        if(codigoDuelo!=null){
            ponTimer();
        }
    }

    public void traePreguntas(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Trayendo datos...");
        progressDialog.setMessage("Trayendo preguntas de firebase");
        progressDialog.setCancelable(false);
        progressDialog.show();
        hayPreguntas = false;

        databaseReference.child(nodoPregunta).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayPreguntas.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Pregunta preguntaa = snapshot.getValue(Pregunta.class);
                        if(preguntaa.getMateria().equalsIgnoreCase(materiaSeleccionada)){
                            arrayPreguntas.add(preguntaa);
                            hayPreguntas = true;
                        }else
                        if(materiaSeleccionada.equals("todas")){
                            arrayPreguntas.add(preguntaa);
                            hayPreguntas = true;
                        }else {
                            hayPreguntas = false;
                            Log.w("Preguntas", "No hay preguntas de esta materia en la base");
                        }
                    }
                    if(hayPreguntas){
                        cargaPregunta(0);
                    }else{
                        Toast.makeText(Preguntas.this, "Lo sentimos aun no tenemos preguntas para esta materia", Toast.LENGTH_SHORT).show();
                        finish();
                    }
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
        txtPregunta.setText(arrayPreguntas.get(index).getPregunta());
        txtRa.setText(arrayPreguntas.get(index).getrA());
        txtRb.setText(arrayPreguntas.get(index).getrB());
        txtRc.setText(arrayPreguntas.get(index).getrC());
        txtRd.setText(arrayPreguntas.get(index).getrD());
        solucion = arrayPreguntas.get(index).getSolucion();

        //Validando que existan imagenes

            if(arrayPreguntas.get(index).getPreguntaImg() != null){
                traeImagen(arrayPreguntas.get(index).getPreguntaImg(), 1, nodoPreguntasImg);
                imgPregunta.setVisibility(View.VISIBLE);
            }else{
                imgPregunta.setVisibility(View.GONE);
            }

            if(arrayPreguntas.get(index).getrAImg() != null){
                traeImagen(arrayPreguntas.get(index).getrAImg(), 2, nodoRespuestasImg);
                imgRa.setVisibility(View.VISIBLE);
            }else {
                imgRa.setVisibility(View.GONE);
            }

            if(arrayPreguntas.get(index).getrBImg() != null){
                traeImagen(arrayPreguntas.get(index).getrBImg(), 3, nodoRespuestasImg);
                imgRb.setVisibility(View.VISIBLE);
            }else {
                imgRb.setVisibility(View.GONE);
            }

            if(arrayPreguntas.get(index).getrCImg() != null){
                traeImagen(arrayPreguntas.get(index).getrCImg(), 4, nodoRespuestasImg);
                imgRc.setVisibility(View.VISIBLE);
            }else {
                imgRc.setVisibility(View.GONE);
            }

            if(arrayPreguntas.get(index).getrDImg() != null){
                traeImagen(arrayPreguntas.get(index).getrDImg(), 5, nodoRespuestasImg);
                imgRd.setVisibility(View.VISIBLE);
            }else {
                imgRd.setVisibility(View.GONE);
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
        String resSeleccionada = v.getTag().toString();
        bande = bande + 1;
        if(bande == 1){
            if(resSeleccionada.equals(solucion)){
                //Si la contesto bien
                contaBuenas = contaBuenas + 1;
                contaTotal = contaTotal + 1;
            }else {
                //Si la contesto mal
                contaMalas = contaMalas + 1;
                contaTotal = contaTotal + 1;
            }

            GradientDrawable viewCorrecto = null;

            if (solucion.equals("A")) {
                viewCorrecto = (GradientDrawable) resA.getBackground();
            } else if (solucion.equals("B")) {
                viewCorrecto = (GradientDrawable) resB.getBackground();
            } else if (solucion.equals("C")) {
                viewCorrecto = (GradientDrawable) resC.getBackground();
            } else if (solucion.equals("D")) {
                viewCorrecto = (GradientDrawable) resD.getBackground();
            }

            viewCorrecto.setStroke(8, this.getResources().getColor(R.color.colorPrimary));

            siguiente.setEnabled(true);
        }
    }

    public void clickeoSiguiente(View v){
        if(arrayPreguntas.size()>contaTotal){
            limpiaCampos();
            bande = 0;
            //Enviando nueva pregunta
            cargaPregunta(contaTotal);
            siguiente.setEnabled(false);
        }else{
            guardaRespuestas();
            muestraPopUp("Ya has contestado todas las preguntas");
        }
    }

    public void limpiaCampos(){
        //LIMPIANDO IMAGENES
        imgPregunta.setImageDrawable(null);
        imgRa.setImageDrawable(null);
        imgRb.setImageDrawable(null);
        imgRc.setImageDrawable(null);
        imgRd.setImageDrawable(null);
        //LIMPIANDO TEXTO
        txtRa.setText("");
        txtRb.setText("");
        txtRc.setText("");
        txtRd.setText("");
        txtPregunta.setText("");
        //LIMPIANDO COLOR BORDES
        GradientDrawable viewA = (GradientDrawable) resA.getBackground();
        GradientDrawable viewB = (GradientDrawable) resB.getBackground();
        GradientDrawable viewC = (GradientDrawable) resC.getBackground();
        GradientDrawable viewD = (GradientDrawable) resD.getBackground();
        viewA.setStroke(8, this.getResources().getColor(R.color.negro));
        viewB.setStroke(8, this.getResources().getColor(R.color.negro));
        viewC.setStroke(8, this.getResources().getColor(R.color.negro));
        viewD.setStroke(8, this.getResources().getColor(R.color.negro));
    }

    public void muestraPopUp(String mensaje){
        TextView txtClose, txtBuenas, txtMalas, txtMensaje;
        miVentana.setContentView(R.layout.my_pop_up);
        txtClose = (TextView) miVentana.findViewById(R.id.txtclose);
        txtBuenas = (TextView) miVentana.findViewById(R.id.txtBuenas);
        txtMalas = (TextView) miVentana.findViewById(R.id.txtMalas);
        txtMensaje = (TextView) miVentana.findViewById(R.id.txtMensaje);
        txtBuenas.setText(Integer.toString(contaBuenas));
        txtMalas.setText(Integer.toString(contaMalas));
        txtMensaje.setText(mensaje);
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

    public void salir(View view){
        guardaRespuestas();
        muestraPopUp("Felicidades...!");
    }

    //------PARA RESPUESTAS--------
    public void traePersona(){
        firebaseAuth = FirebaseAuth.getInstance();
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
                            idPersona = persona.getIdPersona();
                            trae();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void trae(){
        final String nodoMateria = materiaSeleccionada.replace(" ","");
        final String nodoTotalMateria = "total"+nodoMateria;
        databaseReference.child(nodoRespuestas).child(idPersona).child(nodoMateria).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    existe = true;
                    //Si ya existen respuestas para este usuario
                    contaMateria = Integer.parseInt(dataSnapshot.getValue().toString());
                }else{
                    existe = false;
                    //Si aun no existen respuestas para este usuario
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void guardaRespuestas(){
        if(!(materiaSeleccionada.equals("todas"))){
            final String nodoMateria = materiaSeleccionada.replace(" ","");
            final String nodoTotalMateria = "total"+nodoMateria;
           if(existe){
               databaseReference.child(nodoRespuestas).child(idPersona).child(nodoTotalMateria).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists()){
                           existe = true;
                           //Si ya existen respuestas para este usuario
                           contaMateriaTotal = Integer.parseInt(dataSnapshot.getValue().toString());

                           databaseReference.child(nodoRespuestas).child(idPersona).child(nodoMateria).setValue(contaMateria+contaBuenas);
                           databaseReference.child(nodoRespuestas).child(idPersona).child(nodoTotalMateria).setValue(contaMateriaTotal+contaBuenas+contaMalas);
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

           }else{
               databaseReference.child(nodoRespuestas).child(idPersona).child(nodoMateria).setValue(contaBuenas);
               int TotalPreg = contaBuenas+contaMalas;
               databaseReference.child(nodoRespuestas).child(idPersona).child(nodoTotalMateria).setValue(contaBuenas+contaMalas);
           }

        }

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
    //-----------------------------


    //-------PARA TIMER------------

    public void ponTimer(){
        countdownText = (TextView) findViewById(R.id.countdown_text);
        countdownText.setVisibility(View.VISIBLE);
        salir.setVisibility(View.GONE);
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
        txtClose.setVisibility(View.GONE);
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
