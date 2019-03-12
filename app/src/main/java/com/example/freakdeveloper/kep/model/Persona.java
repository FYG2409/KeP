package com.example.freakdeveloper.kep.model;

public class Persona {
    private String nickName;
    private String email;
    private String contra;
    private String eProcedencia;
    private String eDeseada;

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
