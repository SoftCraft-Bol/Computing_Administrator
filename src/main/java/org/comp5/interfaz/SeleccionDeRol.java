package org.comp5.interfaz;

import org.comp5.controllador.DataBase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SeleccionDeRol extends JFrame {
    private JComboBox<String> rolBox;
    private JComboBox<String> usuarioBox;
    private JButton siguienteButton, cancelarButton;

    public SeleccionDeRol() {
        setTitle("Selección de Rol y Usuario");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(243, 250, 252));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        rolBox = new JComboBox<>(new String[]{
                "Seleccionar Rol",
                "Docentes de Pizarra",
                "Docentes a Dedicación Exclusiva",
                "Auxiliares de Pizarra",
                "Administrador de Laboratorio"
        });

        usuarioBox = new JComboBox<>(new String[]{"Seleccionar Usuario"});
        usuarioBox.setEnabled(false);
        rolBox.addActionListener(e -> cargarUsuariosSegunRol());

        int row = 0;
        addFormField(mainPanel, "Seleccionar Rol:", rolBox, gbc, row++);
        addFormField(mainPanel, "Seleccionar Usuario:", usuarioBox, gbc, row++);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(216, 236, 243));

        siguienteButton = crearBoton("Siguiente", new Color(60, 179, 113), Color.WHITE);
        cancelarButton = crearBoton("Cancelar", new Color(255, 99, 71), Color.WHITE);

        siguienteButton.addActionListener(e -> irAReservaLaboratorio());
        cancelarButton.addActionListener(e -> dispose());

        buttonPanel.add(siguienteButton);
        buttonPanel.add(cancelarButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, String label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 12));
        fieldLabel.setForeground(new Color(25, 25, 112));
        panel.add(fieldLabel, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private JButton crearBoton(String texto, Color bgColor, Color fgColor) {
        JButton boton = new JButton(texto);
        boton.setBackground(bgColor);
        boton.setForeground(fgColor);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        return boton;
    }

    private void cargarUsuariosSegunRol() {
        String rolSeleccionado = (String) rolBox.getSelectedItem();
        usuarioBox.removeAllItems();
        usuarioBox.addItem("Seleccionar Usuario");

        if (rolSeleccionado == null || rolSeleccionado.equals("Seleccionar Rol")) {
            usuarioBox.setEnabled(false);
            return;
        }
        DataBase db = new DataBase();
        try {
            List<String> usuarios = db.obtenerUsuariosPorRol(rolSeleccionado);
            if (!usuarios.isEmpty()) {
                usuarioBox.setEnabled(true);
                for (String usuario : usuarios) {
                    usuarioBox.addItem(usuario);
                }
            } else {
                usuarioBox.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void irAReservaLaboratorio() {
        String rolSeleccionado = (String) rolBox.getSelectedItem();
        String usuarioSeleccionado = (String) usuarioBox.getSelectedItem();

        if (rolSeleccionado == null || rolSeleccionado.equals("Seleccionar Rol") ||
                usuarioSeleccionado == null || usuarioSeleccionado.equals("Seleccionar Usuario")) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un rol y un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Redirigir a ReservaLaboratorioForm con los valores seleccionados
        ReservaLaboratorioForm reservaForm = new ReservaLaboratorioForm(rolSeleccionado, usuarioSeleccionado);
        reservaForm.setVisible(true);
        dispose(); // Cierra la ventana actual
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SeleccionDeRol seleccionDeRol = new SeleccionDeRol();
//            seleccionDeRol.setVisible(true);
//        });
//    }
}
