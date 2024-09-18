package com.ac.cachesim;

public class ConfigCache {
    private int tamanioPalabra;
    private int tamanioBloque;
    private int tamanioConjunto;
    private String politicaReemplazo;

    public ConfigCache() {
        tamanioPalabra = 4;
        tamanioBloque = 32;
        tamanioConjunto = 1;
        politicaReemplazo = "LRU";
    }

    public int getTamanioPalabra() {
        return tamanioPalabra;
    }

    public void setTamanioPalabra(int tamanioPalabra) {
        this.tamanioPalabra = tamanioPalabra;
    }

    public int getTamanioBloque() {
        return tamanioBloque;
    }

    public void setTamanioBloque(int tamanioBloque) {
        this.tamanioBloque = tamanioBloque;
    }

    public int getTamanioConjunto() {
        return tamanioConjunto;
    }

    public void setTamanioConjunto(int tamanioConjunto) {
        this.tamanioConjunto = tamanioConjunto;
    }

    public String getPoliticaReemplazo() {
        return politicaReemplazo;
    }

    public void setPoliticaReemplazo(String politicaReemplazo) {
        this.politicaReemplazo = politicaReemplazo;
    }
}

