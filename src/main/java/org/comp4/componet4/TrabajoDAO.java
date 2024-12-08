// Paquete para DAO y conexión
package org.comp4.componet4;

import org.comp4.model.Trabajo;
import org.comp4.MySql.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajoDAO {

    // Método para insertar un nuevo trabajo en la base de datos
    public void insertarTrabajo(Trabajo trabajo) {
        String sql = "INSERT INTO trabajos_mantenimiento (tipo_trabajo, fecha_inicio, fecha_fin,  nombre_responsable, cliente, descripcion_problema, " +
                "descripcion_servicio, estado, costo, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trabajo.getTipoTrabajo());
            stmt.setDate(2, new Date(trabajo.getFechaInicio().getTime()));
            stmt.setDate(3, trabajo.getFechaFin() != null ? new Date(trabajo.getFechaFin().getTime()) : null);
            //stmt.setInt(4, trabajo.getTecnicoId());  // Aquí debes usar el ID del usuario
            stmt.setString(4, trabajo.getTecnico()); // Aquí almacenas el nombre del técnico
            stmt.setString(5, trabajo.getCliente());
            stmt.setString(6, trabajo.getDescripcionProblema());
            stmt.setString(7, trabajo.getDescripcionServicio());
            stmt.setString(8, trabajo.getEstado());
            stmt.setDouble(9, trabajo.getCosto());
            stmt.setString(10, trabajo.getObservaciones());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método para obtener todos los trabajos
    public List<Trabajo> obtenerTodosLosTrabajos() {
        List<Trabajo> trabajos = new ArrayList<>();
        String sql = "SELECT t.*, u.nombre AS nombre_responsable " +
                "FROM trabajos_mantenimiento t " +
                "LEFT JOIN usuarios u ON t.usuarios_id = u.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Trabajo trabajo = new Trabajo(
                        rs.getInt("id"),
                        rs.getString("tipo_trabajo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"),
                        rs.getString("nombre_responsable"), // Nombre del técnico
                        rs.getString("cliente"),
                        rs.getString("descripcion_problema"),
                        rs.getString("descripcion_servicio"),
                        rs.getString("estado"),
                        rs.getDouble("costo"),
                        rs.getString("observaciones")
                );
                trabajos.add(trabajo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trabajos;
    }

    public void eliminarTrabajo(int trabajoId) {
        String query = "DELETE FROM trabajos_mantenimiento WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, trabajoId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Trabajo eliminado correctamente.");
            } else {
                System.out.println("No se encontró el trabajo con ID: " + trabajoId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Trabajo obtenerTrabajoPorId(int trabajoId) {
        String sql = "SELECT * FROM trabajos_mantenimiento WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, trabajoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Trabajo(
                            rs.getInt("id"),
                            rs.getString("tipo_trabajo"),
                            rs.getDate("fecha_inicio"),
                            rs.getDate("fecha_fin"),
                            rs.getString("usuarios_id"),
                            rs.getString("cliente"),
                            rs.getString("descripcion_problema"),
                            rs.getString("descripcion_servicio"),
                            rs.getString("estado"),
                            rs.getDouble("costo"),
                            rs.getString("observaciones")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para actualizar un trabajo existente en la base de datos
    public void actualizarTrabajo(Trabajo trabajo) {
        String sql = "UPDATE trabajos_mantenimiento SET tipo_trabajo = ?, fecha_fin = ?, usuarios_id = ?, cliente = ?, descripcion_problema = ?, " +
                "descripcion_servicio = ?, estado = ?, costo = ?, observaciones = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trabajo.getTipoTrabajo());
            stmt.setDate(2, trabajo.getFechaFin() != null ? new Date(trabajo.getFechaFin().getTime()) : null);
            stmt.setString(3, trabajo.getTecnico());
            stmt.setString(4, trabajo.getCliente());
            stmt.setString(5, trabajo.getDescripcionProblema());
            stmt.setString(6, trabajo.getDescripcionServicio());
            stmt.setString(7, trabajo.getEstado());
            stmt.setDouble(8, trabajo.getCosto());
            stmt.setString(9, trabajo.getObservaciones());
            stmt.setInt(10, trabajo.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
