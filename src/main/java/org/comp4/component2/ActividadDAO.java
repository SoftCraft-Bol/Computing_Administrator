package org.comp4.component2;

import org.comp4.MySql.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAO {

    public void crearActividad(String nombre, String descripcion, Date fechaInicio, Date fechaFin, int responsableId) {
        String query = "INSERT INTO actividades (nombre, descripcion, fecha_inicio, fecha_fin, responsable_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDate(3, fechaInicio);
            stmt.setDate(4, fechaFin);
            stmt.setInt(5, responsableId);
            stmt.executeUpdate();
            System.out.println("Actividad creada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> listarActividades() {
        String query = "SELECT nombre FROM actividades";
        List<String> actividades = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                actividades.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actividades;
    }
}