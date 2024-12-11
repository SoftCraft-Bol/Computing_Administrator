package org.comp4.ui;

import org.comp4.componet3.UsuarioDAO;
import org.comp4.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class VerificacionContrasenaFrame extends JFrame {
    private JPasswordField passwordField;
    private JButton verificarButton;

    private Usuario usuario; // Usuario autenticado previamente.

    public VerificacionContrasenaFrame(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Verificación de Contraseña");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 44, 52));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Ingrese su contraseña para continuar:");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(label);

        mainPanel.add(Box.createVerticalStrut(15));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordField);

        mainPanel.add(Box.createVerticalStrut(15));

        verificarButton = new JButton("Verificar");
        verificarButton.setFont(new Font("Arial", Font.BOLD, 14));
        verificarButton.setBackground(new Color(76, 175, 80));
        verificarButton.setForeground(Color.WHITE);
        verificarButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        verificarButton.addActionListener(e -> verificarContrasena());

        mainPanel.add(verificarButton);

        add(mainPanel);
    }

    private void verificarContrasena() {
        String password = new String(passwordField.getPassword());
        if (usuario.getPassword().equals(password)) {
            JOptionPane.showMessageDialog(this, "Contraseña correcta.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            abrirVentanaAsistencia();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta. Intente de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaAsistencia() {
        AsistenciaFrame asistenciaFrame = new AsistenciaFrame(usuario.getId());
        asistenciaFrame.setVisible(true);
    }
}
