package org.comp4.ui;

import org.comp4.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class PermisosFrame extends JFrame {
    private Usuario usuario;
    private List<String> permisos;

    public PermisosFrame(Usuario usuario, List<String> permisos) {
        this.usuario = usuario;
        this.permisos = permisos;

        setTitle("Permisos de Usuario");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(45, 48, 56));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Cabecera
        JLabel headerLabel = new JLabel("Permisos Asignados para " + usuario.getNombre());
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Panel de botones de permisos
        JPanel permisosPanel = new JPanel();
        permisosPanel.setLayout(new GridLayout(0, 2, 15, 15)); // Dos columnas con espacio entre botones
        permisosPanel.setBackground(new Color(45, 48, 56));

        for (String permiso : permisos) {
            JButton permisoButton = new JButton(permiso);
            permisoButton.setFont(new Font("Arial", Font.BOLD, 14));
            permisoButton.setBackground(new Color(76, 175, 80));
            permisoButton.setForeground(Color.WHITE);
            permisoButton.setFocusPainted(false);
            permisoButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            permisoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Acción para cada botón
            permisoButton.addActionListener(e -> ejecutarAccionPorPermiso(permiso));

            permisosPanel.add(permisoButton);
        }

        JScrollPane scrollPane = new JScrollPane(permisosPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Pie de página con botón cerrar y registrar usuario
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footerPanel.setBackground(new Color(45, 48, 56));

        JButton cerrarButton = new JButton("Cerrar");
        cerrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cerrarButton.setBackground(new Color(244, 67, 54));
        cerrarButton.setForeground(Color.WHITE);
        cerrarButton.setFocusPainted(false);
        cerrarButton.addActionListener(e -> dispose());

        footerPanel.add(cerrarButton);

        // Botón "Registrar Usuario" solo visible para administradores
        if (usuario.getRoles().stream().anyMatch(rol -> rol.getNombre().equals("Administrador"))) {
            JButton registrarUsuarioButton = new JButton("Registrar Usuario");
            registrarUsuarioButton.setFont(new Font("Arial", Font.BOLD, 14));
            registrarUsuarioButton.setBackground(new Color(33, 150, 243));
            registrarUsuarioButton.setForeground(Color.WHITE);
            registrarUsuarioButton.setFocusPainted(false);
            registrarUsuarioButton.addActionListener(e -> abrirFormularioRegistroUsuario());

            footerPanel.add(registrarUsuarioButton);
        }

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void ejecutarAccionPorPermiso(String permiso) {
        if (permiso.equalsIgnoreCase("Gestión de Trabajos de Mantenimiento")) {
            SwingUtilities.invokeLater(() -> {
                TablaMantenimiento tablaMantenimientoFrame = new TablaMantenimiento();
                tablaMantenimientoFrame.setVisible(true);
            });
        } else if (permiso.equalsIgnoreCase("Control de Asistencia")) {
            abrirVerificacionContrasena();
        }else if (permiso.equalsIgnoreCase("Gestión de Reservas")){
            SwingUtilities.invokeLater(() -> {
                GestionReservasFrame gestionReservasFrame = new GestionReservasFrame();
                gestionReservasFrame.setVisible(true);
            });


        }else if (permiso.equalsIgnoreCase("Gestión de Componentes")){
        SwingUtilities.invokeLater(() -> {
            GestionComponentesFrame gestionComponentesFrame = new GestionComponentesFrame();
            gestionComponentesFrame.setVisible(true);
        });
    }else if (permiso.equalsIgnoreCase("Control de Acceso a Laboratorios")){
        SwingUtilities.invokeLater(() -> {
            GestionAccesosFrame gestionAccesosFrame = new GestionAccesosFrame();
            gestionAccesosFrame.setVisible(true);
        });
    }else if (permiso.equalsIgnoreCase("Gestión de usuarios")){
            SwingUtilities.invokeLater(() -> {
                UserManagementUI managementUI = new UserManagementUI();
                managementUI.setVisible(true);
            });
        }
    else {
        JOptionPane.showMessageDialog(this, "Permiso no implementado: " + permiso, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    }


    private void abrirVerificacionContrasena() {
        VerificacionContrasenaFrame verificacionFrame = new VerificacionContrasenaFrame(usuario);
        verificacionFrame.setVisible(true);
    }

    private void abrirFormularioRegistroUsuario() {
        UserManagementUI registroUsuarioFrame = new UserManagementUI();
        registroUsuarioFrame.setVisible(true);
    }
}
