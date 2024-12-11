package org.comp4.componet3;


import org.comp4.MySql.DatabaseConnection;
import org.comp4.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {
    public List<Rol> obtenerRolesSecundarios(int idRolPadre) {
        List<Rol> roles = new ArrayList<>();
        String query = "SELECT id, nombre FROM roles WHERE id_rol_padre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRolPadre);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setId(rs.getInt("id"));
                    rol.setNombre(rs.getString("nombre"));
                    roles.add(rol);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public List<Rol> obtenerRoles() {
        List<Rol> roles = new ArrayList<>();
        String query = "SELECT * FROM roles";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId(rs.getInt("id"));
                rol.setNombre(rs.getString("nombre"));
                rol.setIdRolPadre(rs.getInt("id_rol_padre"));
                roles.add(rol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

}

