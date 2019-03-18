package com.example.freakdeveloper.kep.model;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class Pregunta {


    private String materia;
    private String pregunta;
    private String rA;
    private String rB;
    private String rC;
    private String rD;
    private String solucion;
    private String preguntaImg;
    private String rAImg;
    private String rBImg;
    private String rCImg;
    private String rDImg;

    public Pregunta() {
    }

    public Pregunta(String materia, String pregunta, String rA, String rB, String rC, String rD, String solucion, String preguntaImg, String rAImg, String rBImg, String rCImg, String rDImg) {
        this.materia = materia;
        this.pregunta = pregunta;
        this.rA = rA;
        this.rB = rB;
        this.rC = rC;
        this.rD = rD;
        this.solucion = solucion;
        this.preguntaImg = preguntaImg;
        this.rAImg = rAImg;
        this.rBImg = rBImg;
        this.rCImg = rCImg;
        this.rDImg = rDImg;
    }

    public Pregunta(String pregunta, String rA, String rB, String rC, String rD, String solucion, String preguntaImg, String rAImg, String rBImg, String rCImg, String rDImg) {
        this.pregunta = pregunta;
        this.rA = rA;
        this.rB = rB;
        this.rC = rC;
        this.rD = rD;
        this.solucion = solucion;
        this.preguntaImg = preguntaImg;
        this.rAImg = rAImg;
        this.rBImg = rBImg;
        this.rCImg = rCImg;
        this.rDImg = rDImg;
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

    public String getPreguntaImg() {
        return preguntaImg;
    }

    public void setPreguntaImg(String preguntaImg) {
        this.preguntaImg = preguntaImg;
    }

    public String getrAImg() {
        return rAImg;
    }

    public void setrAImg(String rAImg) {
        this.rAImg = rAImg;
    }

    public String getrBImg() {
        return rBImg;
    }

    public void setrBImg(String rBImg) {
        this.rBImg = rBImg;
    }

    public String getrCImg() {
        return rCImg;
    }

    public void setrCImg(String rCImg) {
        this.rCImg = rCImg;
    }

    public String getrDImg() {
        return rDImg;
    }

    public void setrDImg(String rDImg) {
        this.rDImg = rDImg;
    }
}
