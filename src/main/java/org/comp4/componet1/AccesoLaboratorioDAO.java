package org.comp4.componet1;

import org.comp4.MySql.DatabaseConnection;
import org.comp4.model.AccesoLaboratorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccesoLaboratorioDAO {

    public List<AccesoLaboratorio> obtenerAccesos() {
        List<AccesoLaboratorio> accesos = new ArrayList<>();
        String query = "SELECT * FROM acceso_laboratorio";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                AccesoLaboratorio acceso = new AccesoLaboratorio();
                acceso.setId(rs.getInt("id"));
                acceso.setLaboratorioId(rs.getInt("laboratorio_id"));
                acceso.setUsuarioId(rs.getInt("usuario_id"));
                acceso.setFechaAcceso(rs.getDate("fecha_acceso"));
                acceso.setHoraEntrada(rs.getTime("hora_entrada"));
                acceso.setHoraSalida(rs.getTime("hora_salida"));
                acceso.setMotivoAcceso(rs.getString("motivo_acceso"));
                accesos.add(acceso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accesos;
    }

    public void registrarAcceso(AccesoLaboratorio acceso) {
        String query = "INSERT INTO acceso_laboratorio (laboratorio_id, usuario_id, fecha_acceso, hora_entrada, hora_salida, motivo_acceso) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, acceso.getLaboratorioId());
            pstmt.setInt(2, acceso.getUsuarioId());
            pstmt.setDate(3, acceso.getFechaAcceso());
            pstmt.setTime(4, acceso.getHoraEntrada());
            pstmt.setTime(5, acceso.getHoraSalida());
            pstmt.setString(6, acceso.getMotivoAcceso());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verificarReserva(int laboratorioId, int usuarioId, Date fecha) {
        String query = "SELECT COUNT(*) FROM reservas WHERE laboratorio_id = ? AND usuario_id = ? AND fecha = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, laboratorioId);
            pstmt.setInt(2, usuarioId);
            pstmt.setDate(3, fecha);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void eliminarAcceso(int id) {
        String query = "DELETE FROM acceso_laboratorio WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
