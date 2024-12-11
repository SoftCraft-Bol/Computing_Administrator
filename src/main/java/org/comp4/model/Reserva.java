package org.comp4.model;

import java.util.Date;

public class Reserva {
    private int id;
    private int laboratorioId;
    private int usuarioId;
    private Date fecha;
    private String hora;
    private String topico;

    // Constructor vacío
    public Reserva() {}

    // Constructor completo
    public Reserva(int id, int laboratorioId, int usuarioId, Date fecha, String hora, String topico) {
        this.id = id;
        this.laboratorioId = laboratorioId;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.hora = hora;
        this.topico = topico;
    }

    // Getters y Setters
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }
}
