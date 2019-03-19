package com.example.freakdeveloper.kep.model;

public class Duelo {
    private String correoPerUno;
    private String correoPerDos;
    private Long totalBuenasUno;
    private Long totalBuenasDos;
    private Long numAleatorio;
    private String tipoDuelo;
    private Long totalPreguntas;
    private Long malas;

    public Duelo() {
    }

    public Duelo(String correoPerUno, String correoPerDos, Long totalBuenasUno, Long totalBuenasDos, Long numAleatorio, String tipoDuelo, Long totalPreguntas, Long malas) {
        this.correoPerUno = correoPerUno;
        this.correoPerDos = correoPerDos;
        this.totalBuenasUno = totalBuenasUno;
        this.totalBuenasDos = totalBuenasDos;
        this.numAleatorio = numAleatorio;
        this.tipoDuelo = tipoDuelo;
        this.totalPreguntas = totalPreguntas;
        this.malas = malas;
    }

    public Duelo(String correoPerUno, String correoPerDos, Long totalBuenasUno, Long totalBuenasDos, Long numAleatorio, String tipoDuelo, Long totalPreguntas) {
        this.correoPerUno = correoPerUno;
        this.correoPerDos = correoPerDos;
        this.totalBuenasUno = totalBuenasUno;
        this.totalBuenasDos = totalBuenasDos;
        this.numAleatorio = numAleatorio;
        this.tipoDuelo = tipoDuelo;
        this.totalPreguntas = totalPreguntas;
    }

    public Long getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(Long totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    public String getTipoDuelo() {
        return tipoDuelo;
    }

    public void setTipoDuelo(String tipoDuelo) {
        this.tipoDuelo = tipoDuelo;
    }

    public Long getNumAleatorio() {
        return numAleatorio;
    }

    public void setNumAleatorio(Long numAleatorio) {
        this.numAleatorio = numAleatorio;
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

    public Long getTotalBuenasUno() {
        return totalBuenasUno;
    }

    public void setTotalBuenasUno(Long totalBuenasUno) {
        this.totalBuenasUno = totalBuenasUno;
    }

    public Long getTotalBuenasDos() {
        return totalBuenasDos;
    }

    public void setTotalBuenasDos(Long totalBuenasDos) {
        this.totalBuenasDos = totalBuenasDos;
    }
}
