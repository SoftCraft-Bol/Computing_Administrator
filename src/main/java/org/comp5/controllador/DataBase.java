package org.comp5.controllador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_mantenimiento2";
    private static final String USER = "alfredo";
    private static final String PASSWORD = "notebok456";
    private Connection connection;

    public DataBase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL exitosa.");
        } catch (SQLException e) {
            System.out.println("Error en la conexión a MySQL: " + e.getMessage());
        }
    }

    public List<String> obtenerRoles() throws SQLException {
        List<String> roles = new ArrayList<>();
        String query = "SELECT nombre FROM roles";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roles.add(rs.getString("nombre"));
            }
        }

        return roles;
    }

    public List<String> obtenerUsuariosPorRol(String rolNombre) throws SQLException {
        List<String> usuarios = new ArrayList<>();
        String query = """
                            SELECT u.nombre
                            FROM usuarios u
                            INNER JOIN usuarios_roles ur ON u.id = ur.usuario_id
                            INNER JOIN roles r ON ur.rol_id = r.id
                            WHERE r.nombre = ?
                        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, rolNombre);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(rs.getString("nombre"));
                }
            }
        }

        return usuarios;
    }

    public void registrarReserva(int laboratorioId, int usuarioId, Date fecha, Time hora, Time duracion, String topico) throws SQLException {
        String query = """
        INSERT INTO reservas (laboratorio_id, usuario_id, fecha, hora, duracion, topico)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, laboratorioId);
            stmt.setInt(2, usuarioId);
            stmt.setDate(3, fecha);
            stmt.setTime(4, hora);
            stmt.setTime(5, duracion);
            stmt.setString(6, topico);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Reserva registrada exitosamente.");
            }
        }
    }

    public List<Object[]> obtenerReservasActivas() throws SQLException {
        List<Object[]> reservas = new ArrayList<>();
        String query = """
        SELECT r.id, l.nombre AS laboratorio, r.fecha, r.hora, r.duracion, u.nombre AS usuario, r.topico
        FROM reservas r
        INNER JOIN laboratorio l ON r.laboratorio_id = l.id
        INNER JOIN usuarios u ON r.usuario_id = u.id
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] reserva = {
                        rs.getInt("id"),
                        rs.getString("laboratorio"),
                        rs.getDate("fecha"),
                        rs.getTime("hora"),
                        rs.getTime("duracion"),
                        rs.getString("usuario"),
                        rs.getString("topico")
                };
                reservas.add(reserva);
            }
        }

        return reservas;
    }

    public int obtenerLaboratorioIdPorNombre(String nombreLaboratorio) throws SQLException {
        String query = "SELECT id FROM laboratorio WHERE nombre = ?";
        int laboratorioId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombreLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    laboratorioId = rs.getInt("id");
                }
            }
        }

        return laboratorioId;
    }

    public int obtenerUsuarioIdPorNombre(String nombreUsuario) throws SQLException {
        String query = "SELECT id FROM usuarios WHERE nombre = ?";
        int usuarioId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuarioId = rs.getInt("id");
                }
            }
        }

        return usuarioId;
    }
    public List<String> obtenerLaboratorios() throws SQLException {
        List<String> laboratorios = new ArrayList<>();
        String query = "SELECT nombre FROM laboratorio";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                laboratorios.add(rs.getString("nombre"));
            }
        }

        return laboratorios;
    }
}
