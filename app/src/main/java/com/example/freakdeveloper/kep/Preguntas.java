package com.example.freakdeveloper.kep;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Preguntas extends AppCompatActivity {
    //PARA ALEATORIO
    private int numAleatorio, totalPreguntas, conta;
    private Pregunta preguntaObj;
    private int contaMaterias;
    private Boolean todasMaterias;
    String[] opcMaterias = {"Razonamiento Matematico", "Algebra", "Geometria y Trigonometria", "Geometria Analitica", "Calculo Diferencial e Integral", "Probabilidad y Estadistica", "Produccion Escrita", "Comprension de Textos", "Biologia", "Quimica", "Fisica"};

    private int bande;

    private TextView txtPregunta;
    private Button salir;
    private TextView txtRa, txtRb, txtRc, txtRd;
    private LinearLayout resA, resB, resC, resD;

    private ImageView imgRa, imgRb, imgRc, imgRd, imgPregunta, siguiente;
    private ArrayList<Pregunta> arrayPreguntas = new ArrayList<>();
    private int contaBuenas, contaMalas, contaTotal, numInicio;
    private String codigoDuelo, tipoPersona, email, materiaSeleccionada, solucion, tipoDuelo;

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
    private long tiempoEnMilisegundos = (5*60000); //Son los minutos que queremos en milisegundos

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

        existe = false;

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();
        todasMaterias = false;
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
        contaMaterias = 0;

        //PARA POP-UP
        miVentana = new Dialog(this);
        limpiaCampos();
        //traePreguntas();
        traePersona();

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salir();
            }
        });
    }

    //PARA ALEATORIO
    public void generarNumAleatorio(){
        numAleatorio = (int) (Math.random() * totalPreguntas) + 1;
        traePregunta();
    }

    public void traePregunta() {
        if(todasMaterias){
            materiaSeleccionada = opcMaterias[contaMaterias];
        }

        databaseReference.child(nodoPregunta).child(materiaSeleccionada).child(Integer.toString(numAleatorio)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Si existe la pregunta
                    if(dataSnapshot.getKey().equals(Integer.toString(numAleatorio))){
                        //Si el id de la pregunta es igual a mi numero aleatorio
                        preguntaObj = dataSnapshot.getValue(Pregunta.class);
                        muestraPregunta();
                        //arrayPreguntas.add(preguntaa);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void muestraPregunta(){
        txtPregunta.setText(preguntaObj.getPregunta());
        txtRa.setText(preguntaObj.getrA());
        txtRb.setText(preguntaObj.getrB());
        txtRc.setText(preguntaObj.getrC());
        txtRd.setText(preguntaObj.getrD());
        solucion = preguntaObj.getSolucion();

        //Validando que existan imagenes

        if(preguntaObj.getPreguntaImg() != null){
            traeImagen(preguntaObj.getPreguntaImg(), 1, nodoPreguntasImg);
            imgPregunta.setVisibility(View.VISIBLE);
        }else{
            imgPregunta.setVisibility(View.GONE);
        }

        if(preguntaObj.getrAImg() != null){
            traeImagen(preguntaObj.getrAImg(), 2, nodoRespuestasImg);
            imgRa.setVisibility(View.VISIBLE);
        }else {
            imgRa.setVisibility(View.GONE);
        }

        if(preguntaObj.getrBImg() != null){
            traeImagen(preguntaObj.getrBImg(), 3, nodoRespuestasImg);
            imgRb.setVisibility(View.VISIBLE);
        }else {
            imgRb.setVisibility(View.GONE);
        }

        if(preguntaObj.getrCImg() != null){
            traeImagen(preguntaObj.getrCImg(), 4, nodoRespuestasImg);
            imgRc.setVisibility(View.VISIBLE);
        }else {
            imgRc.setVisibility(View.GONE);
        }

        if(preguntaObj.getrDImg() != null){
            traeImagen(preguntaObj.getrDImg(), 5, nodoRespuestasImg);
            imgRd.setVisibility(View.VISIBLE);
        }else {
            imgRd.setVisibility(View.GONE);
        }
    }

    public void recuperaDatosIntent(){
        Bundle extras = getIntent().getExtras();
        materiaSeleccionada = extras.getString("materia");
        email = extras.getString("email");
        totalPreguntas = extras.getInt("totalHijos");

        if(materiaSeleccionada.equals("todas")){
            todasMaterias = true;
        }else{
            todasMaterias = false;
        }

        //EXTRAS CUANDO VENGO DE DueloFragment
        tipoPersona = extras.getString("tipoPersona");
        codigoDuelo = extras.getString("codigoDuelo");
        tipoDuelo = extras.getString("tipoDuelo");
        numInicio = extras.getInt("numInicio");
        if(codigoDuelo!=null && tipoDuelo.equals("contraTiempo")){
            ponTimer();
        }

        generarNumAleatorio();
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

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Preguntas", "Error al traer imagen: "+ e.toString());
            }
        });
    }

    public void clickeo(View v){

        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

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
                //agregando vibracion
                vibrator.vibrate(500);
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

    public void clickeoSiguiente(View v){

        siguiente.setEnabled(false);
        if (numAleatorio == totalPreguntas) {
            numAleatorio = 1;
        } else {
            numAleatorio = numAleatorio + 1;
        }

        if(todasMaterias){
            if(contaMaterias==(opcMaterias.length-1)){
                contaMaterias = 0;
            }else{
                contaMaterias = contaMaterias + 1;
            }
        }

        traePregunta();
        bande = 0;
        limpiaCampos();
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
        miVentana.setCancelable(false);
        miVentana.show();
    }

    public void salir(){
        if(codigoDuelo!=null){
            //CUANDO ABANDONAMOS UN DUELO
            LinearLayout marcadores;
            TextView txtClose, txtMensaje;
            miVentana.setContentView(R.layout.my_pop_up);
            txtClose = (TextView) miVentana.findViewById(R.id.txtclose);
            marcadores = (LinearLayout) miVentana.findViewById(R.id.marcadores);
            txtMensaje = (TextView) miVentana.findViewById(R.id.txtMensaje);
            marcadores.setVisibility(View.GONE);
            txtMensaje.setText("Abandonaste la partida");
            if(tipoPersona.equals("Uno")){
                databaseReference.child(nodoDuelos).child(codigoDuelo).removeValue();
            }else
            if(tipoPersona.equals("Dos")){
                databaseReference.child(nodoDuelos).child(codigoDuelo).removeValue();
            }
            miVentana.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            miVentana.setCancelable(false);
            miVentana.show();
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    miVentana.dismiss();
                    finish();
                }
            });

        }else{
            guardaRespuestas();
            muestraPopUp("Felicidades...!");
        }
    }

    public void escuchaDuelo(){
        databaseReference.child(nodoDuelos).child(codigoDuelo).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    conta = conta + 1;
                    if(conta == 1){
                        //LA PERSONA ABANDONO EL DUELO
                        LinearLayout marcadores;
                        TextView txtClose, txtMensaje;
                        miVentana.setContentView(R.layout.my_pop_up);
                        txtClose = (TextView) miVentana.findViewById(R.id.txtclose);
                        marcadores = (LinearLayout) miVentana.findViewById(R.id.marcadores) ;
                        txtMensaje = (TextView) miVentana.findViewById(R.id.txtMensaje);
                        marcadores.setVisibility(View.GONE);
                        txtMensaje.setText("La otra persona abandono el duelo");
                        miVentana.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        miVentana.setCancelable(false);
                        miVentana.show(); //AQUI HAY PROBLEMAS
                        txtClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                miVentana.dismiss();
                                finish();
                            }
                        });
                    }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        if(codigoDuelo==null){
            if(!todasMaterias){
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
        //salir = (Button) findViewById(R.id.salir);
        //salir.setVisibility(View.GONE);
        iniciaTimer();
        escuchaDuelo();
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
                    }

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
        miVentana.setCancelable(false);
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

                        databaseReference.child(nodoDuelos).child(codigoDuelo).removeValue();

                        txtMensaje.setText(msj);
                        txtBuenas.setText(Integer.toString(contaBuenas));
                        txtMalas.setText(Integer.toString(contaMalas));
                        txtClose.setVisibility(View.VISIBLE);
                        txtClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                miVentana.dismiss();
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            salir();
        }
        return super.onKeyDown(keyCode, event);
    }

}
