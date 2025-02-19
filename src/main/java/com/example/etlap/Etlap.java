package com.example.etlap;

public class Etlap {
    private int id;
    private String nev;
    private String kategoria;
    private int ar;
    private String leiras;

    public Etlap(int id, String nev, String kategoria, int ar, String leiras) {
        this.id = id;
        this.nev = nev;
        this.kategoria = kategoria;
        this.ar = ar;
        this.leiras = leiras;
    }

    public int getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

    public String getKategoria() {
        return kategoria;
    }

    public int getAr() {
        return ar;
    }

    public String getLeiras() {
        return leiras;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    @Override
    public String toString() {
        return nev + " - " + ar + " Ft";
    }
}


