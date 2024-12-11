package org.comp4.ui;

import org.comp4.model.Usuario;
import org.comp4.model.Rol;
import org.comp4.componet3.UsuarioDAO;
import org.comp4.componet3.RolDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagementUI extends JFrame {

    private UsuarioDAO usuarioDAO;
    private RolDAO rolDAO;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementUI() {
        usuarioDAO = new UsuarioDAO();
        rolDAO = new RolDAO();

        setTitle("Gestión de Usuarios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior con botones
        JPanel topPanel = new JPanel();
        JButton addButton = new JButton("Agregar Usuario");
        JButton editButton = new JButton("Editar Usuario");
        JButton deleteButton = new JButton("Eliminar Usuario");

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        add(topPanel, BorderLayout.NORTH);

        // Tabla de usuarios
        String[] columnNames = {"ID", "Nombre", "Email", "Disponibilidad", "Carga de Trabajo", "Roles"};

        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        cargarUsuariosEnTabla();

        JScrollPane tableScrollPane = new JScrollPane(userTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Eventos de botones
        addButton.addActionListener(e -> mostrarFormularioRegistro());
        editButton.addActionListener(e -> mostrarFormularioEdicion());
        deleteButton.addActionListener(e -> eliminarUsuarioSeleccionado());
    }

    private void cargarUsuariosEnTabla() {
        tableModel.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();
        for (Usuario usuario : usuarios) {
            // Crear una lista de nombres de roles
            StringBuilder roles = new StringBuilder();
            for (Rol rol : usuario.getRoles()) {
                if (roles.length() > 0) {
                    roles.append(", ");
                }
                roles.append(rol.getNombre());
            }

            tableModel.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getEmail(),
                    usuario.getDisponibilidad(),
                    usuario.getCargaTrabajo(),
                    roles.toString() // Mostrar roles como texto en la tabla
            });
        }
    }


    private void mostrarFormularioRegistro() {
        JDialog dialog = new JDialog(this, "Registrar Usuario", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2));

        JLabel nameLabel = new JLabel("Nombre:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField();
        JLabel disponibilidadLabel = new JLabel("Disponibilidad:");
        JTextField disponibilidadField = new JTextField();
        JLabel cargaLabel = new JLabel("Carga de Trabajo:");
        JTextField cargaField = new JTextField();

        JLabel roleLabel = new JLabel("Rol:");
        JComboBox<Rol> roleComboBox = new JComboBox<>();
        cargarRolesEnComboBox(roleComboBox);

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            Usuario usuario = new Usuario();
            usuario.setNombre(nameField.getText());
            usuario.setEmail(emailField.getText());
            usuario.setPassword(new String(passwordField.getPassword()));
            usuario.setDisponibilidad(disponibilidadField.getText());

            try {
                usuario.setCargaTrabajo(Integer.parseInt(cargaField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La carga de trabajo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuario.getRoles() == null) {
                usuario.setRoles(new ArrayList<>());
            }

            Rol rolSeleccionado = (Rol) roleComboBox.getSelectedItem();
            if (rolSeleccionado != null) {
                usuario.getRoles().add(rolSeleccionado);
            }

            if (usuarioDAO.registrarUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.");
                cargarUsuariosEnTabla();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(emailLabel);
        dialog.add(emailField);
        dialog.add(passwordLabel);
        dialog.add(passwordField);
        dialog.add(disponibilidadLabel);
        dialog.add(disponibilidadField);
        dialog.add(cargaLabel);
        dialog.add(cargaField);
        dialog.add(roleLabel);
        dialog.add(roleComboBox);
        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setVisible(true);
    }

    private void cargarRolesEnComboBox(JComboBox<Rol> roleComboBox) {
        List<Rol> roles = rolDAO.obtenerRoles();
        for (Rol rol : roles) {
            roleComboBox.addItem(rol);
        }
    }

    private void mostrarFormularioEdicion() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para editar.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        Usuario usuario = usuarioDAO.obtenerUsuarioPorId(userId);
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Editar Usuario", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2));

        JLabel nameLabel = new JLabel("Nombre:");
        JTextField nameField = new JTextField(usuario.getNombre());
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(usuario.getEmail());
        JLabel passwordLabel = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField(usuario.getPassword());
        JLabel disponibilidadLabel = new JLabel("Disponibilidad:");
        JTextField disponibilidadField = new JTextField(usuario.getDisponibilidad());
        JLabel cargaLabel = new JLabel("Carga de Trabajo:");
        JTextField cargaField = new JTextField(String.valueOf(usuario.getCargaTrabajo()));

        JLabel roleLabel = new JLabel("Rol:");
        JComboBox<Rol> roleComboBox = new JComboBox<>();
        cargarRolesEnComboBox(roleComboBox);
        if (!usuario.getRoles().isEmpty()) {
            roleComboBox.setSelectedItem(usuario.getRoles().get(0));
        }

        JButton saveButton = new JButton("Guardar Cambios");
        saveButton.addActionListener(e -> {
            usuario.setNombre(nameField.getText());
            usuario.setEmail(emailField.getText());
            usuario.setPassword(new String(passwordField.getPassword()));
            usuario.setDisponibilidad(disponibilidadField.getText());
            try {
                usuario.setCargaTrabajo(Integer.parseInt(cargaField.getText()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La carga de trabajo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Rol rolSeleccionado = (Rol) roleComboBox.getSelectedItem();
            if (rolSeleccionado != null) {
                usuario.getRoles().clear();
                usuario.getRoles().add(rolSeleccionado);
            }

            if (usuarioDAO.editarUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuario editado con éxito.");
                cargarUsuariosEnTabla();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al editar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(emailLabel);
        dialog.add(emailField);
        dialog.add(passwordLabel);
        dialog.add(passwordField);
        dialog.add(disponibilidadLabel);
        dialog.add(disponibilidadField);
        dialog.add(cargaLabel);
        dialog.add(cargaField);
        dialog.add(roleLabel);
        dialog.add(roleComboBox);
        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setVisible(true);
    }

    private void eliminarUsuarioSeleccionado() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        if (usuarioDAO.eliminarUsuario(userId)) {
            JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.");
            cargarUsuariosEnTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManagementUI ui = new UserManagementUI();
            ui.setVisible(true);
        });
    }
}
