package org.comp4.model;

import java.sql.Time;
import java.sql.Date;

public class AccesoLaboratorio {
    private int id;
    private int laboratorioId;
    private int usuarioId;
    private Date fechaAcceso;
    private Time horaEntrada;
    private Time horaSalida;
    private String motivoAcceso;

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLaboratorioId() {
        return laboratorioId;
    }

    public void setLaboratorioId(int laboratorioId) {
        this.laboratorioId = laboratorioId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Date getFechaAcceso() {
        return fechaAcceso;
    }

    public void setFechaAcceso(Date fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }

    public Time getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Time horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getMotivoAcceso() {
        return motivoAcceso;
    }

    public void setMotivoAcceso(String motivoAcceso) {
        this.motivoAcceso = motivoAcceso;
    }
}
