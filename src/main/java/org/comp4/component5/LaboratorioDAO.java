package org.comp4.component5;

import org.comp4.MySql.DatabaseConnection;
import org.comp4.model.Laboratorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioDAO {

    // Método para obtener todos los laboratorios
    public List<Laboratorio> listarLaboratorios() {
        List<Laboratorio> laboratorios = new ArrayList<>();
        String query = "SELECT id, nombre FROM laboratorio"; // Suponiendo que tienes una tabla de laboratorios
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Laboratorio laboratorio = new Laboratorio();
                laboratorio.setId(rs.getInt("id"));
                laboratorio.setNombre(rs.getString("nombre"));
                laboratorios.add(laboratorio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laboratorios;
    }

    // Método para obtener un laboratorio por su ID
    public Laboratorio obtenerLaboratorioPorId(int id) {
        String query = "SELECT * FROM laboratorio WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Laboratorio laboratorio = new Laboratorio();
                    laboratorio.setId(rs.getInt("id"));
                    laboratorio.setNombre(rs.getString("nombre"));
                    laboratorio.setCapacidad(rs.getInt("capacidad"));
                    laboratorio.setAula(rs.getString("aula"));
                    return laboratorio;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para agregar un nuevo laboratorio
    public boolean agregarLaboratorio(Laboratorio laboratorio) {
        String query = "INSERT INTO laboratorio (nombre, capacidad, aula) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, laboratorio.getNombre());
            pstmt.setInt(2, laboratorio.getCapacidad());
            pstmt.setString(3, laboratorio.getAula());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para actualizar un laboratorio
    public boolean actualizarLaboratorio(Laboratorio laboratorio) {
        String query = "UPDATE laboratorio SET nombre = ?, capacidad = ?, aula = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, laboratorio.getNombre());
            pstmt.setInt(2, laboratorio.getCapacidad());
            pstmt.setString(3, laboratorio.getAula());
            pstmt.setInt(4, laboratorio.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para eliminar un laboratorio
    public boolean eliminarLaboratorio(int id) {
        String query = "DELETE FROM laboratorio WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
