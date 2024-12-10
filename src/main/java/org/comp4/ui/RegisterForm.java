package org.comp4.ui;

import org.comp4.componet3.RolDAO;
import org.comp4.componet3.UsuarioDAO;
import org.comp4.model.Rol;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegisterForm extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<Rol> roleComboBox;

    public RegisterForm() {
        setTitle("Registro de Usuario");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(40, 44, 52));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createFieldPanel("Nombre:", nameField = new JTextField()));
        mainPanel.add(createFieldPanel("Email:", emailField = new JTextField()));
        mainPanel.add(createFieldPanel("Contraseña:", passwordField = new JPasswordField()));

        // Selector de roles
        roleComboBox = new JComboBox<>();
        loadRoles();

        JPanel rolePanel = new JPanel();
        rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.Y_AXIS));
        rolePanel.setBackground(new Color(40, 44, 52));
        JLabel roleLabel = new JLabel("Seleccione un Rol:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(Color.WHITE);

        rolePanel.add(roleLabel);
        rolePanel.add(roleComboBox);

        mainPanel.add(rolePanel);

        JButton registerButton = new JButton("Registrar");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> register());

        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(registerButton);

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
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);

        return panel;
    }

    private void loadRoles() {
        RolDAO rolDAO = new RolDAO();
        List<Rol> roles = rolDAO.obtenerRolesSecundarios(0); // Cargar roles principales

        for (Rol rol : roles) {
            roleComboBox.addItem(rol);
        }
    }

    private void register() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        Rol selectedRole = (Rol) roleComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRole == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.registrarUsuario(name, email, password, selectedRole.getId());

        JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        new LoginForm().setVisible(true);
        dispose();
    }
}
