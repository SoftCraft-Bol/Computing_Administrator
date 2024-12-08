package org.comp4.model;


public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String password;
    private String disponibilidad;
    private int cargaTrabajo;

    // Constructor, getters y setters
    public Usuario(int id, String nombre, String email, String password, String disponibilidad, int cargaTrabajo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.disponibilidad = disponibilidad;
        this.cargaTrabajo = cargaTrabajo;
    }

    public Usuario() {}

    // Getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getDisponibilidad() { return disponibilidad; }
    public int getCargaTrabajo() { return cargaTrabajo; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
    public void setCargaTrabajo(int cargaTrabajo) { this.cargaTrabajo = cargaTrabajo; }
}
