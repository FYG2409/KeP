package com.example.freakdeveloper.kep.model;

<<<<<<< HEAD
public class Persona
{
    private String idPersona;
    private String NickName;
    private String EscActual;
    private String Escingresar;
    private String Email;
    private String Contra;


    public Persona(String idPersona, String NickName, String EscActual, String EscIngresar , String Email , String Contra)
    {

        this.idPersona=idPersona;
        this.NickName=NickName;
        this.EscActual=EscActual;
        this.Escingresar=EscIngresar;
        this.Email = Email;
        this.Contra = Contra;
    }

    public String getContra()
    {
        return Contra;
    }

    public void setContra(String contra) {
        Contra = contra;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(String idPersona) {
        this.idPersona = idPersona;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }


    public String getEscActual() {
        return EscActual;
    }

    public void setEscActual(String escActual) {
        EscActual = escActual;
    }

    public String getEscingresar() {
        return Escingresar;
    }

    public void setEscingresar(String escingresar) {
        Escingresar = escingresar;
    }
}
=======
public class Persona {
    private String nickName;
    private String email;
    private String contra;
    private String eProcedencia;
    private String eDeseada;
    private String codigo;

    public Persona() {
    }

    public Persona(String nickName, String email, String contra, String eProcedencia, String eDeseada) {
        this.nickName = nickName;
        this.email = email;
        this.contra = contra;
        this.eProcedencia = eProcedencia;
        this.eDeseada = eDeseada;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String geteProcedencia() {
        return eProcedencia;
    }

    public void seteProcedencia(String eProcedencia) {
        this.eProcedencia = eProcedencia;
    }

    public String geteDeseada() {
        return eDeseada;
    }

    public void seteDeseada(String eDeseada) {
        this.eDeseada = eDeseada;
    }
}
>>>>>>> c92fe5801a5dde638b2c4ece7603cd657778087c
