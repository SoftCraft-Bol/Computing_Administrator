package org.comp4.model;

import java.util.Date;

public class Trabajo {
    private int id;
    private String tipoTrabajo;
    private Date fechaInicio;
    private Date fechaFin;
    private String tecnico;
    private String cliente;
    private String descripcionProblema;
    private String descripcionServicio;
    private String estado;
    private double costo;
    private String observaciones;

    // Constructor, getters y setters
    public Trabajo(int id, String tipoTrabajo, Date fechaInicio, Date fechaFin, String tecnico, String cliente,
                   String descripcionProblema, String descripcionServicio, String estado, double costo, String observaciones) {
        this.id = id;
        this.tipoTrabajo = tipoTrabajo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tecnico = tecnico;
        this.cliente = cliente;
        this.descripcionProblema = descripcionProblema;
        this.descripcionServicio = descripcionServicio;
        this.estado = estado;
        this.costo = costo;
        this.observaciones = observaciones;
    }

    public Trabajo() {
        this.fechaInicio = new Date(); // Set current date as start date by default
    }

    public int getId() {
        return id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public String getTipoTrabajo() {
        return tipoTrabajo;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public String getTecnico() {
        return tecnico;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDescripcionProblema() {
        return descripcionProblema;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public String getEstado() {
        return estado;
    }

    public double getCosto() {
        return costo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipoTrabajo(String tipoTrabajo) {
        this.tipoTrabajo = tipoTrabajo;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setDescripcionProblema(String descripcionProblema) {
        this.descripcionProblema = descripcionProblema;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
