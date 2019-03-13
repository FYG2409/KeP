package com.example.freakdeveloper.kep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.freakdeveloper.kep.model.Carrera;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroCarrera extends AppCompatActivity {

    private String idCarreraTxt, areaTxt, escuelaTxt, carreraTxt;
    private Spinner AreaS, EscuelaS, CarreraS;
    private String areas,escuelas,carreras;

    //PARA FIREBASE
    private DatabaseReference databaseReference;

    private  static final String nodoCarrera="Carreras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_carrera);


        //PARA FIREBASE
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        AreaS = (Spinner) findViewById(R.id.ASpinner);
        String[] diciplina={"Area","Ingeniería y Ciencias Físico Matemáticas","Ciencias Médico Biológicas",
                            "Ciencias Sociales y Administrativas"};
        AreaS.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, diciplina));

        EscuelaS = (Spinner) findViewById(R.id.ESpinner);
        String[] ipn={"Escuela","UPIBI","UPIIZ Campus Zacatecas",
                "UPIITA","ENCB","UPIIG Campus Guanajuato","ESIA Unidad Zacatenco",
                "ESIME Unidad Zacatenco","ESIME Unidad Ticomán","ESIME Unidad Culhuacán",
                "UPIICSA","ESIQIE","ESIME Unidad Azcapotzalco","UPIIH Campus Hidalgo",
                "ESCOM","ESIA Unidad Ticomán","ESFM","ESIT","ESIA Unidad Tecamachalco",
                "CICS Unidad Milpa Alta","CICS Unidad Santo Tomás","ESEO","ENMyH",
                "ESM","ESCA Unidad Santo Tomás","ESCA Unidad Tepepan","ESE","EST"};
        EscuelaS.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ipn));

        CarreraS = (Spinner) findViewById(R.id.CSpinner);
        String[] ingenieria={"Carrera","Ingeniería Ambiental","Ingeniería Biomédica","Ingeniería Biónica",
                "Ingeniería Bioquímica","Ingeniería Biotecnológica","Ingeniería Civil",
                "Ingeniería Eléctrica","Ingeniería en Aeronáutica","Ingeniería en Alimentos",
                "Ingeniería en Computación","Ingeniería en Comunicaciones y Eléctronica",
                "Ingeniería en Control y Automatización","Ingeniería en Informática",
                "Ingeniería en Metalurgia y Materiales","Ingeniería en Robótica Industrial",
                "Ingeniería en Sistemas Ambientales","Ingeniería en Sistemas Automotrices",
                "Ingeniería en Sistemas Computacionales","Ingeniería en Transporte",
                "Ingeniería Farmacéutica","Ingeniería Geofísica","Ingeniería Geológica",
                "Ingeniería Industrial","Ingeniería Matemática","Ingeniería Mecánica",
                "Ingeniería Mecatrónica","Ingeniería Metalúrgica","Ingeniería Petrolera",
                "Ingeniería Química Industrial","Ingeniería Química Petrolera","Ingeniería Telemática",
                "Ingeniería Textil","Ingeniería Topográfica y Fotogramétrica","Ingeniero Arquitecto",
                "Licenciatura en Ciencias de la Informática","Licenciatura en Física y Matemáticas",
                "Licenciado en Nutrición","Licenciado en Optometría","Licenciado en Psicología",
                "Licenciatura en Biología","Licenciatura en Enfermería","Licenciatura en Enfermería y Obstetricia",
                "Licenciatura en Odontología","Licenciatura en Trabajo Social",
                "Médico Cirujano Homeópata","Médico Cirujano Partero","Químico Bacteriólogico Parasitólogo",
                "Químico Farmacéutico Industrial","Contador Público","Licenciatura en Administración Industrial",
                "Licenciatura en Administración y Desarrollo Empresarial","Licenciatura en Economía",
                "Licenciatura en Negocios Internacionales","Licenciatura en Relaciones Comerciales",
                "Licenciatura en Turismo","Licenciatura en Comercio Internacional"};
        CarreraS.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ingenieria));


    }

    public void creaEscuela(View view){
        areas=AreaS.getSelectedItem().toString();
        escuelas=EscuelaS.getSelectedItem().toString();
        carreras=CarreraS.getSelectedItem().toString();
        String junto=escuelas+" "+carreras;

        Carrera carrera = new Carrera(databaseReference.push().getKey(),areas, junto);
        databaseReference.child(nodoCarrera).child(carrera.getIdCarrera()).setValue(carrera);
        Toast.makeText(this, "Carrera Registrada", Toast.LENGTH_SHORT).show();
    }

}
