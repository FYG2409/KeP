package com.example.freakdeveloper.kep.model;

public class Duelo {
    private String correoPerUno;
    private String correoPerDos;
    private String correoGanador;

    public Duelo() {
    }

    public Duelo(String correoPerDos, String correoGanador) {
        this.correoPerDos = correoPerDos;
        this.correoGanador = correoGanador;
    }

    public Duelo(String correoPerUno, String correoPerDos, String correoGanador) {
        this.correoPerUno = correoPerUno;
        this.correoPerDos = correoPerDos;
        this.correoGanador = correoGanador;
    }

    public String getCorreoPerUno() {
        return correoPerUno;
    }

    public void setCorreoPerUno(String correoPerUno) {
        this.correoPerUno = correoPerUno;
    }

    public String getCorreoPerDos() {
        return correoPerDos;
    }

    public void setCorreoPerDos(String correoPerDos) {
        this.correoPerDos = correoPerDos;
    }

    public String getCorreoGanador() {
        return correoGanador;
    }

    public void setCorreoGanador(String correoGanador) {
        this.correoGanador = correoGanador;
    }
}
