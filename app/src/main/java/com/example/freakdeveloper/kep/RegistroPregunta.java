package com.example.freakdeveloper.kep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.freakdeveloper.kep.model.Pregunta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistroPregunta extends AppCompatActivity {

    private EditText idPregunta, pregunta, rA, rB, rC, rD;
    private String idPreguntaTxt, materiaTxt, preguntaTxt, rATxt, rBTxt, rCTxt, rDTxt, solucionTxt;

    //PARA FIREBASE
    private DatabaseReference databaseReference;
    private  static final String nodoPregunta="Preguntas";

    //AGREGAR IMAGEN
    private ImageView imgPregunta, imgRepuestaA, imgRepuestaB, imgRepuestaC, imgRepuestaD;
    private StorageReference storageRef;
    private String refFinalImg;
    private Uri uriPregunta = null, uriRespuestaA= null, uriRespuestaB= null, uriRespuestaC= null, uriRespuestaD= null;
    private ProgressDialog progressDialog;
    private int conta = 0;
    private int totalImagenes = 5;

    private String refFinalPreguntaImg = null,refFinalRespuestaAImg = null, refFinalRespuestaBImg = null, refFinalRespuestaCImg = null, refFinalRespuestaDImg = null;

    private static final String nodoPreguntasImg="Preguntas/";
    private static final String nodoRespuestasImg="Respuestas/";

    //SPINNER
    private Spinner spMaterias, spSolucion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_pregunta);

        pregunta = (EditText) findViewById(R.id.pregunta);
        rA = (EditText) findViewById(R.id.rA);
        rB = (EditText) findViewById(R.id.rB);
        rC = (EditText) findViewById(R.id.rC);
        rD = (EditText) findViewById(R.id.rD);

        //PARA FIREBASE
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //AGREGAR IMAGEN
        storageRef = FirebaseStorage.getInstance().getReference();
        imgPregunta= (ImageView) findViewById(R.id.imgPregunta);
        imgRepuestaA = (ImageView) findViewById(R.id.imgRepuestaA);
        imgRepuestaB = (ImageView) findViewById(R.id.imgRepuestaB);
        imgRepuestaC = (ImageView) findViewById(R.id.imgRepuestaC);
        imgRepuestaD = (ImageView) findViewById(R.id.imgRepuestaD);
        progressDialog = new ProgressDialog(this);

        //SPINNER
        spMaterias = (Spinner) findViewById(R.id.spMaterias);
        spSolucion = (Spinner) findViewById(R.id.spSolucion);
        llenaSpinners();

    }

    public void guardaPregunta(View view){
        if(uriPregunta==null){
            totalImagenes = totalImagenes - 1;
        }
        if(uriRespuestaA==null){
            totalImagenes = totalImagenes - 1;
        }
        if(uriRespuestaB==null){
            totalImagenes = totalImagenes - 1;
        }
        if(uriRespuestaC==null){
            totalImagenes = totalImagenes - 1;
        }
        if(uriRespuestaD==null){
            totalImagenes = totalImagenes - 1;
        }

        if(totalImagenes==0){
            gPregunta();
        }


        if(uriPregunta!=null){
            subeImagen(uriPregunta, nodoPreguntasImg, 1);
        }
        if(uriRespuestaA!=null){
            subeImagen(uriRespuestaA, nodoRespuestasImg, 2);
        }
        if(uriRespuestaB!=null){
            subeImagen(uriRespuestaB, nodoRespuestasImg, 3);
        }
        if(uriRespuestaC!=null){
            subeImagen(uriRespuestaC, nodoRespuestasImg, 4);
        }
        if(uriRespuestaD!=null){
            subeImagen(uriRespuestaD, nodoRespuestasImg, 5);
        }
    }

    public void gPregunta(){
        Log.w("FEIK", "ENTRE x2");
        materiaTxt = spMaterias.getSelectedItem().toString();
        solucionTxt = spSolucion.getSelectedItem().toString();
        preguntaTxt = pregunta.getText().toString();
        rATxt = rA.getText().toString();
        rBTxt = rB.getText().toString();
        rCTxt = rC.getText().toString();
        rDTxt = rD.getText().toString();
        Pregunta pregunta = new Pregunta(materiaTxt, preguntaTxt, rATxt, rBTxt, rCTxt, rDTxt, solucionTxt, refFinalPreguntaImg, refFinalRespuestaAImg, refFinalRespuestaBImg, refFinalRespuestaCImg, refFinalRespuestaDImg);
        databaseReference.child(nodoPregunta).child(databaseReference.push().getKey()).setValue(pregunta);
        Toast.makeText(this, "PREGUNTA REGISTRADA", Toast.LENGTH_SHORT).show();
        Log.w("FEIK", "SUBI");
    }

    //------PARA IMAGEN------

    public void traeImagenDeGaleria(View v){
        int reqCode = 0;
        if(v.getTag().equals("btnPregunta")){
            reqCode = 1;
        }else
        if(v.getTag().equals("btnRespuestaA")){
            reqCode = 2;
        }else
        if(v.getTag().equals("btnRespuestaB")){
            reqCode = 3;
        }else
        if(v.getTag().equals("btnRespuestaC")){
            reqCode = 4;
        }else
        if(v.getTag().equals("btnRespuestaD")){
            reqCode = 5;
        }

        if(reqCode!=0){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/");
            startActivityForResult(intent.createChooser(intent, "Seleccione la aplicacion"),reqCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri uri = data.getData();
            if(requestCode==1){
                imgPregunta.setImageURI(uri);
                uriPregunta = uri;
            }else
            if(requestCode==2){
                imgRepuestaA.setImageURI(uri);
                uriRespuestaA = uri;
            }else
            if(requestCode==3){
                imgRepuestaB.setImageURI(uri);
                uriRespuestaB = uri;
            }else
            if(requestCode==4){
                imgRepuestaC.setImageURI(uri);
                uriRespuestaC = uri;
            }else
            if(requestCode==5){
                imgRepuestaD.setImageURI(uri);
                uriRespuestaD = uri;
            }

        }
    }

    public void subeImagen(final Uri uri, String nodo, final int reqCode){
        progressDialog.setTitle("Subiendo...");
        progressDialog.setMessage("Subiendo imagen a firebase");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Uri file = uri;

        final StorageReference imagenRef = storageRef.child(nodo+file.getLastPathSegment());

        final UploadTask uploadTask = imagenRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("RegistroPregunta", "Error al subir imagen: "+ e.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                refFinalImg = imagenRef.getName();
                Log.w("FEIK", "Imagenes subida correctamente");

                if(reqCode==1){
                    refFinalPreguntaImg = refFinalImg;
                }else
                if(reqCode==2){
                    refFinalRespuestaAImg = refFinalImg;
                }else
                if(reqCode==3){
                    refFinalRespuestaBImg = refFinalImg;
                }else
                if(reqCode==4){
                    refFinalRespuestaCImg = refFinalImg;
                }else
                if(reqCode==5){
                    refFinalRespuestaDImg = refFinalImg;
                }

                conta = conta + 1;
                Log.w("FEIK", "Conta: "+conta);
                if(conta == totalImagenes){
                    progressDialog.dismiss();
                    Log.w("FEIK", "ENTRE");
                    gPregunta();
                }
            }
        });
    }

    public void llenaSpinners(){
        String[] opcMaterias = {"Razonamiento Matematico", "Algebra", "Geometria y Trigonometria", "Geometria Analitica", "Calculo Diferencial e Integral", "Probabilidad y Estadistica", "Produccion Escrita", "Comprension de Textos", "Biologia", "Quimica", "Fisica"};
        ArrayAdapter<String> adapterMaterias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opcMaterias);
        spMaterias.setAdapter(adapterMaterias);

        String[] opcSoluciones = {"A", "B", "C", "D"};
        ArrayAdapter<String> adapterSoluciones = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opcSoluciones);
        spSolucion.setAdapter(adapterSoluciones);
    }

/*
    public void regresaImagen(Task<Uri> uriTask, final int reqCode){
        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView img = null;
                if(reqCode==1){
                    img = imgPregunta;
                }else
                if(reqCode==2){
                    img = imgRepuestaA;
                }else
                if(reqCode==3){
                    img = imgRepuestaB;
                }else
                if(reqCode==4){
                    img = imgRepuestaC;
                }else
                if(reqCode==5){
                    img = imgRepuestaD;
                }

                Glide.with(RegistroPregunta.this)
                        .load(uri)
                        .into(img);

                Log.w("RegistroPregunta", "Imagen traida correctamente");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("RegistroPregunta", "Error al traer imagen: "+ e.toString());
            }
        });
    }

*/
    //------------------------

}