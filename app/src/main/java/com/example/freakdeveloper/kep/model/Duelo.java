package com.example.freakdeveloper.kep.model;

public class Duelo {
    private String id;
    private String correoPerUno;
    private String correoPerDos;
    private String correoGanador;

    private String codigo;

    public Duelo() {
    }

    public Duelo(String correoPerUno, String correoPerDos, String correoGanador, String codigo) {
        this.correoPerUno = correoPerUno;
        this.correoPerDos = correoPerDos;
        this.correoGanador = correoGanador;
        this.codigo = codigo;
    }

    public Duelo(String id, String correoPerUno, String correoPerDos, String correoGanador, String codigo) {
        this.id = id;
        this.correoPerUno = correoPerUno;
        this.correoPerDos = correoPerDos;
        this.correoGanador = correoGanador;
        this.codigo = codigo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


}
