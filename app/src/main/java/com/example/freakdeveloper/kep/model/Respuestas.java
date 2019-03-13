package com.example.freakdeveloper.kep.model;

public class Respuestas {
    private int RM; //Razonamiento Matematico
    private int A; //Algebra
    private int GyT; //Geometria y Trigonometria
    private int GA; //Geometria Analitica
    private int CDeI; //Calculo Diferencial e Integral
    private int PyE; //Probabilidad y Estadistica
    private int CT; //Comprencion de Textos
    private int B; //Biologia
    private int Q; //Quimica
    private int F; //Fisica
    private int tRM; //Razonamiento Matematico
    private int tA; //Algebra
    private int tGyT; //Geometria y Trigonometria
    private int tGA; //Geometria Analitica
    private int tCDeI; //Calculo Diferencial e Integral
    private int tPyE; //Probabilidad y Estadistica
    private int tCT; //Comprencion de Textos
    private int tB; //Biologia
    private int tQ; //Quimica
    private int tF; //Fisica

    public Respuestas() {
    }

    public Respuestas(int RM, int a, int gyT, int GA, int CDeI, int pyE, int CT, int b, int q, int f, int tRM, int tA, int tGyT, int tGA, int tCDeI, int tPyE, int tCT, int tB, int tQ, int tF) {
        this.RM = RM;
        A = a;
        GyT = gyT;
        this.GA = GA;
        this.CDeI = CDeI;
        PyE = pyE;
        this.CT = CT;
        B = b;
        Q = q;
        F = f;
        this.tRM = tRM;
        this.tA = tA;
        this.tGyT = tGyT;
        this.tGA = tGA;
        this.tCDeI = tCDeI;
        this.tPyE = tPyE;
        this.tCT = tCT;
        this.tB = tB;
        this.tQ = tQ;
        this.tF = tF;
    }

    public int getRM() {
        return RM;
    }

    public void setRM(int RM) {
        this.RM = RM;
    }

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getGyT() {
        return GyT;
    }

    public void setGyT(int gyT) {
        GyT = gyT;
    }

    public int getGA() {
        return GA;
    }

    public void setGA(int GA) {
        this.GA = GA;
    }

    public int getCDeI() {
        return CDeI;
    }

    public void setCDeI(int CDeI) {
        this.CDeI = CDeI;
    }

    public int getPyE() {
        return PyE;
    }

    public void setPyE(int pyE) {
        PyE = pyE;
    }

    public int getCT() {
        return CT;
    }

    public void setCT(int CT) {
        this.CT = CT;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getQ() {
        return Q;
    }

    public void setQ(int q) {
        Q = q;
    }

    public int getF() {
        return F;
    }

    public void setF(int f) {
        F = f;
    }

    public int gettRM() {
        return tRM;
    }

    public void settRM(int tRM) {
        this.tRM = tRM;
    }

    public int gettA() {
        return tA;
    }

    public void settA(int tA) {
        this.tA = tA;
    }

    public int gettGyT() {
        return tGyT;
    }

    public void settGyT(int tGyT) {
        this.tGyT = tGyT;
    }

    public int gettGA() {
        return tGA;
    }

    public void settGA(int tGA) {
        this.tGA = tGA;
    }

    public int gettCDeI() {
        return tCDeI;
    }

    public void settCDeI(int tCDeI) {
        this.tCDeI = tCDeI;
    }

    public int gettPyE() {
        return tPyE;
    }

    public void settPyE(int tPyE) {
        this.tPyE = tPyE;
    }

    public int gettCT() {
        return tCT;
    }

    public void settCT(int tCT) {
        this.tCT = tCT;
    }

    public int gettB() {
        return tB;
    }

    public void settB(int tB) {
        this.tB = tB;
    }

    public int gettQ() {
        return tQ;
    }

    public void settQ(int tQ) {
        this.tQ = tQ;
    }

    public int gettF() {
        return tF;
    }

    public void settF(int tF) {
        this.tF = tF;
    }
}
