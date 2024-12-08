package org.comp4.ui;

import org.comp4.componet4.UsuarioDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        setTitle("Login - Sistema de Mantenimiento");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(40, 44, 52)); // Fondo oscuro
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Cabecera
        JLabel headerLabel = new JLabel("Inicio de Sesión");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Panel del formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(40, 44, 52));

        // Campo de Usuario
        formPanel.add(createFieldPanel("Usuario:", usernameField = new JTextField()));

        // Campo de Contraseña
        formPanel.add(createFieldPanel("Contraseña:", passwordField = new JPasswordField()));

        // Botón de Login
        loginButton = createButton("Iniciar Sesión", new Color(76, 175, 80));
        loginButton.addActionListener(e -> login());

        // Panel para el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(40, 44, 52));
        buttonPanel.add(loginButton);

        formPanel.add(Box.createVerticalStrut(15)); // Espacio antes del botón
        formPanel.add(buttonPanel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(40, 44, 52));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(400, 30));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5)); // Espacio entre el label y el campo
        panel.add(field);

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuarioDAO.validarLogin(username, password)) {
            JOptionPane.showMessageDialog(this, "Login exitoso");
            new TablaMantenimiento().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
