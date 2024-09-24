package com.ac.cachesim;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TableRow {

    @JsonProperty("conjunto")
    private int conjunto;
    @JsonProperty("valido")
    private int valido;
    @JsonProperty("modificado")
    private int modificado;
    @JsonProperty("etiqueta")
    private int etiqueta;
    @JsonProperty("tiempo")
    private int tiempo;
    @JsonProperty("bloqueMP")
    private int bloqueMP;

    public TableRow() {
    }

    public TableRow(int conjunto, int valido, int modificado, int etiqueta, int tiempo, int bloqueMP) {
        this.conjunto = conjunto;
        this.valido = valido;
        this.modificado = modificado;
        this.etiqueta = etiqueta;
        this.tiempo = tiempo;
        this.bloqueMP = bloqueMP;
    }

    public int getConjunto() {
        return conjunto;
    }

    public void setConjunto(int conjunto) {
        this.conjunto = conjunto;
    }

    public int getValido() {
        return valido;
    }

    public void setValido(int valido) {
        this.valido = valido;
    }

    public int getBloqueMP() {
        return bloqueMP;
    }

    public void setBloqueMP(int bloqueMP) {
        this.bloqueMP = bloqueMP;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getModificado() {
        return modificado;
    }

    public void setModificado(int modificado) {
        this.modificado = modificado;
    }

    public int getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(int etiqueta) {
        this.etiqueta = etiqueta;
    }
}
