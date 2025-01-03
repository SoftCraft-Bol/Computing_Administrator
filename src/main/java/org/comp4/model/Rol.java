package org.comp4.model;

public class Rol {
    private int id;
    private String nombre;
    private int idRolPadre;

    public Rol(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Rol() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdRolPadre(int idRolPadre) {
        this.idRolPadre = idRolPadre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
