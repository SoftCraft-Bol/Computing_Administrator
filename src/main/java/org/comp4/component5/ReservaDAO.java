package org.comp4.component5;

import org.comp4.model.Reserva;
import org.comp4.MySql.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public List<Reserva> listarReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String query = "SELECT * FROM reservas";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva reserva = new Reserva();
                reserva.setId(rs.getInt("id"));
                reserva.setLaboratorioId(rs.getInt("laboratorio_id"));
                reserva.setUsuarioId(rs.getInt("usuario_id"));
                reserva.setFecha(rs.getDate("fecha"));
                reserva.setHora(rs.getString("hora"));
                reserva.setTopico(rs.getString("topico"));
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas: " + e.getMessage());
            e.printStackTrace();
        }
        return reservas;
    }

    public boolean agregarReserva(Reserva reserva) {
        String query = "INSERT INTO reservas (laboratorio_id, usuario_id, fecha, hora, topico) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, reserva.getLaboratorioId());
            pstmt.setInt(2, reserva.getUsuarioId());
            pstmt.setDate(3, new java.sql.Date(reserva.getFecha().getTime()));
            pstmt.setString(4, reserva.getHora());
            pstmt.setString(5, reserva.getTopico());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reserva agregada exitosamente.");
                return true;
            } else {
                System.err.println("No se pudo agregar la reserva. Verifica los datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean editarReserva(Reserva reserva) {
        String query = "UPDATE reservas SET laboratorio_id = ?, usuario_id = ?, fecha = ?, hora = ?, topico = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, reserva.getLaboratorioId());
            pstmt.setInt(2, reserva.getUsuarioId());
            pstmt.setDate(3, new java.sql.Date(reserva.getFecha().getTime()));
            pstmt.setString(4, reserva.getHora());
            pstmt.setString(5, reserva.getTopico());
            pstmt.setInt(6, reserva.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminarReserva(int idReserva) {
        String query = "DELETE FROM reservas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idReserva);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Reserva obtenerReservaPorId(int idReserva) {
        Reserva reserva = null;
        String query = "SELECT * FROM reservas WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idReserva);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reserva = new Reserva();
                    reserva.setId(rs.getInt("id"));
                    reserva.setLaboratorioId(rs.getInt("laboratorio_id"));
                    reserva.setUsuarioId(rs.getInt("usuario_id"));
                    reserva.setFecha(rs.getDate("fecha"));
                    reserva.setHora(rs.getString("hora"));
                    reserva.setTopico(rs.getString("topico"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reserva;
    }

    public List<Reserva> listarReservasPorUsuario(int idUsuario) {
        List<Reserva> reservas = new ArrayList<>();
        String query = "SELECT * FROM reservas WHERE usuario_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reserva reserva = new Reserva();
                    reserva.setId(rs.getInt("id"));
                    reserva.setLaboratorioId(rs.getInt("laboratorio_id"));
                    reserva.setUsuarioId(rs.getInt("usuario_id"));
                    reserva.setFecha(rs.getDate("fecha"));
                    reserva.setHora(rs.getString("hora"));
                    reserva.setTopico(rs.getString("topico"));
                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
}
