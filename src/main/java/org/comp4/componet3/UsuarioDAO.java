package org.comp4.componet3;

import org.comp4.model.Rol;
import org.comp4.model.Usuario;
import org.comp4.MySql.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public boolean validarLogin(String email, String password) {
        String query = "SELECT COUNT(*) AS total FROM usuarios WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Usuario> obtenerUsuariosDisponibles() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT id, nombre FROM usuarios WHERE disponibilidad = 'Disponible'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }


    public List<Rol> obtenerRolesUsuario(int idUsuario) {
        List<Rol> roles = new ArrayList<>();
        String query = "SELECT r.id, r.nombre FROM roles r " +
                "JOIN usuarios_roles ur ON r.id = ur.rol_id " +
                "WHERE ur.usuario_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

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

    public Usuario obtenerUsuarioPorEmail(String email) {
        Usuario usuario = null;
        String query = "SELECT id, nombre, email, password, disponibilidad, carga_trabajo FROM usuarios WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setDisponibilidad(rs.getString("disponibilidad"));
                    usuario.setCargaTrabajo(rs.getInt("carga_trabajo"));

                    // Obtener roles del usuario
                    usuario.setRoles(obtenerRolesUsuario(usuario.getId()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public List<String> obtenerPermisosCompletos(int idRol) {
        List<String> permisos = new ArrayList<>();
        String query = "WITH RECURSIVE rol_hierarchy AS ( " +
                "    SELECT id, id_rol_padre FROM roles WHERE id = ? " +
                "    UNION ALL " +
                "    SELECT r.id, r.id_rol_padre " +
                "    FROM roles r " +
                "    INNER JOIN rol_hierarchy rh ON rh.id_rol_padre = r.id " +
                ") " +
                "SELECT rp.permiso " +
                "FROM rol_hierarchy rh " +
                "JOIN roles_permisos rp ON rp.id_rol = rh.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRol);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permisos.add(rs.getString("permiso"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permisos;
    }

    public List<String> obtenerPermisosUsuario(int idUsuario) {
        List<String> permisos = new ArrayList<>();
        String query = "SELECT DISTINCT p.nombre " +
                "FROM usuarios_roles ur " +
                "JOIN roles_permisos rp ON ur.rol_id = rp.rol_id " +
                "JOIN permisos p ON rp.permiso_id = p.id " +
                "WHERE ur.usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permisos.add(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permisos;
    }


    public void registrarUsuario(String nombre, String email, String password, int rolId) {
        String query = "INSERT INTO usuarios (nombre, email, password, disponibilidad, carga_trabajo) VALUES (?, ?, ?, 'Disponible', 0)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Establecer los valores para el usuario
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, password);  // En un entorno real, asegúrate de encriptar la contraseña antes de guardarla

            // Ejecutar la inserción
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Obtener el ID del nuevo usuario insertado
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);

                        // Asignar el rol al usuario
                        asignarRolUsuario(userId, rolId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void asignarRolUsuario(int userId, int rolId) {
        String query = "INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Establecer los valores para la relación usuario-rol
            stmt.setInt(1, userId);
            stmt.setInt(2, rolId);

            // Ejecutar la inserción
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
