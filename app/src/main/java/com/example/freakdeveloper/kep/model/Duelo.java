package com.example.freakdeveloper.kep.model;

public class Duelo {
    private String correoPerUno;
    private String correoPerDos;
    private Long totalBuenasUno;
    private Long totalBuenasDos;

    public Duelo() {
    }

    public Duelo(String correoPerUno, String correoPerDos, Long totalBuenasUno, Long totalBuenasDos) {
        this.correoPerUno = correoPerUno;
        this.correoPerDos = correoPerDos;
        this.totalBuenasUno = totalBuenasUno;
        this.totalBuenasDos = totalBuenasDos;
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
