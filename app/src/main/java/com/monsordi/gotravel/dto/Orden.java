package com.monsordi.gotravel.dto;

import java.io.Serializable;
import java.util.List;

public class Orden implements Serializable {

    Long id;

    Usuario usuario;

    Almacen almacen;

    Celular celular;

    Encargado encargado;

    Asesor asesor;

    List<PrestadorServicio> prestadores;

    Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public Encargado getEncargado() {
        return encargado;
    }

    public void setEncargado(Encargado encargado) {
        this.encargado = encargado;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public List<PrestadorServicio> getPrestadores() {
        return prestadores;
    }

    public void setPrestadores(List<PrestadorServicio> prestadores) {
        this.prestadores = prestadores;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}
