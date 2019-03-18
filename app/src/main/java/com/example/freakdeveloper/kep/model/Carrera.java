package com.example.freakdeveloper.kep.model;

public class Carrera
{

    private String Area;
    private String Carrera;
    private String idCarrera;

    public Carrera()
    {

    }

    public Carrera(String idCarrera, String Area, String Carrera) {

        this.Area = Area;
        this.Carrera = Carrera;
        this.idCarrera = idCarrera;
    }

    public String getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(String idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String Area) {
        this.Area = Area;
    }


    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String Carrera) {
        this.Carrera = Carrera;
    }
}