package org.comp4.componet1;

import org.comp4.MySql.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    public void registrarEntrada(int usuarioId, String observaciones) throws SQLException {
        String sql = "INSERT INTO asistencia (usuario_id, fecha, hora_entrada, estado, observaciones) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setTime(3, Time.valueOf(LocalTime.now()));
            stmt.setString(4, "Presente");
            stmt.setString(5, observaciones);
            stmt.executeUpdate();
        }
    }

    public void registrarSalida(int usuarioId, String observaciones) throws SQLException {
        String sql = "UPDATE asistencia SET hora_salida = ?, observaciones = ? WHERE usuario_id = ? AND fecha = ? AND hora_salida IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTime(1, Time.valueOf(LocalTime.now()));
            stmt.setString(2, observaciones);
            stmt.setInt(3, usuarioId);
            stmt.setDate(4, Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        }
    }

    public boolean verificarEntrada(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM asistencia WHERE usuario_id = ? AND fecha = ? AND hora_salida IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public List<String[]> obtenerAsistenciasParaTabla(int usuarioId) throws SQLException {
        String sql = "SELECT fecha, hora_entrada, hora_salida, estado, observaciones FROM asistencia WHERE usuario_id = ? ORDER BY fecha DESC";
        List<String[]> asistencias = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fecha = rs.getDate("fecha").toString();
                String horaEntrada = rs.getTime("hora_entrada") != null ? rs.getTime("hora_entrada").toString() : "N/A";
                String horaSalida = rs.getTime("hora_salida") != null ? rs.getTime("hora_salida").toString() : "N/A";
                String estado = rs.getString("estado");
                String observaciones = rs.getString("observaciones") != null ? rs.getString("observaciones") : "Ninguna";

                asistencias.add(new String[]{fecha, horaEntrada, horaSalida, estado, observaciones});
            }
        }

        return asistencias;
    }


}
