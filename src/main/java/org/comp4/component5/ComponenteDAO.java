package org.comp4.component5;

import org.comp4.MySql.DatabaseConnection;
import org.comp4.model.Componente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComponenteDAO {

    public List<Componente> obtenerComponentes() {
        List<Componente> componentes = new ArrayList<>();
        String query = "SELECT * FROM componentes";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Componente componente = new Componente();
                componente.setId(rs.getInt("id"));
                componente.setNombre(rs.getString("nombre"));
                componente.setCantidadEnStock(rs.getInt("cantidad_en_stock"));
                componente.setPrecio(rs.getDouble("precio"));
                componentes.add(componente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return componentes;
    }

    public Componente obtenerComponentePorId(int id) {
        Componente componente = null;
        String query = "SELECT * FROM componentes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    componente = new Componente();
                    componente.setId(rs.getInt("id"));
                    componente.setNombre(rs.getString("nombre"));
                    componente.setCantidadEnStock(rs.getInt("cantidad_en_stock"));
                    componente.setPrecio(rs.getDouble("precio"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return componente;
    }

    public void agregarComponente(Componente componente) {
        String query = "INSERT INTO componentes (nombre, cantidad_en_stock, precio) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, componente.getNombre());
            pstmt.setInt(2, componente.getCantidadEnStock());
            pstmt.setDouble(3, componente.getPrecio());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarComponente(Componente componente) {
        String query = "UPDATE componentes SET nombre = ?, cantidad_en_stock = ?, precio = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, componente.getNombre());
            pstmt.setInt(2, componente.getCantidadEnStock());
            pstmt.setDouble(3, componente.getPrecio());
            pstmt.setInt(4, componente.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarComponente(int id) {
        String query = "DELETE FROM componentes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}