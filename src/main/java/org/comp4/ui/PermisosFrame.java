package org.comp4.ui;

import org.comp4.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PermisosFrame extends JFrame {
    private Usuario usuario;

    public PermisosFrame(Usuario usuario, List<String> permisos) {
        this.usuario    = usuario;

        setTitle("Permisos de Usuario");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(45, 48, 56));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Cabecera
        JLabel headerLabel = new JLabel("Permisos Asignados para " + usuario.getNombre());
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Panel de botones de permisos
        JPanel permisosPanel = new JPanel();
        permisosPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Dos columnas con espacio entre botones
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

        // Pie de página con botón cerrar
        JButton cerrarButton = new JButton("Cerrar");
        cerrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cerrarButton.setBackground(new Color(244, 67, 54));
        cerrarButton.setForeground(Color.WHITE);
        cerrarButton.setFocusPainted(false);
        cerrarButton.addActionListener(e -> dispose());

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(45, 48, 56));
        footerPanel.add(cerrarButton);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void ejecutarAccionPorPermiso(String permiso) {
        if (permiso.equalsIgnoreCase("Control de Asistencia")) {
            abrirVerificacionContrasena();
        } else {
            JOptionPane.showMessageDialog(this, "Acción para: " + permiso, "Permiso seleccionado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirVerificacionContrasena() {
        VerificacionContrasenaFrame verificacionFrame = new VerificacionContrasenaFrame(usuario);
        verificacionFrame.setVisible(true);
    }
}
