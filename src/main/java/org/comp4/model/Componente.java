package org.comp4.model;

public class Componente {
    private int id;
    private String nombre;
    private int cantidadEnStock;
    private double precio;

    public Componente(int id, String nombre, int cantidadEnStock, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadEnStock = cantidadEnStock;
        this.precio = precio;
    }

    public Componente() {}

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

    public int getCantidadEnStock() {
        return cantidadEnStock;
    }

    public void setCantidadEnStock(int cantidadEnStock) {
        this.cantidadEnStock = cantidadEnStock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
