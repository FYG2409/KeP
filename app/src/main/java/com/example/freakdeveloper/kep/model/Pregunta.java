package com.example.freakdeveloper.kep.model;

public class Pregunta {

    private String idPregunta;
    private String materia;
    private String pregunta;
    private String rA;
    private String rB;
    private String rC;
    private String rD;
    private String solucion;

    public Pregunta() {
    }

    public Pregunta(String idPregunta, String materia, String pregunta, String rA, String rB, String rC, String rD, String solucion) {
        this.idPregunta = idPregunta;
        this.materia = materia;
        this.pregunta = pregunta;
        this.rA = rA;
        this.rB = rB;
        this.rC = rC;
        this.rD = rD;
        this.solucion = solucion;
    }

    public String getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(String idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getrA() {
        return rA;
    }

    public void setrA(String rA) {
        this.rA = rA;
    }

    public String getrB() {
        return rB;
    }

    public void setrB(String rB) {
        this.rB = rB;
    }

    public String getrC() {
        return rC;
    }

    public void setrC(String rC) {
        this.rC = rC;
    }

    public String getrD() {
        return rD;
    }

    public void setrD(String rD) {
        this.rD = rD;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }
}
