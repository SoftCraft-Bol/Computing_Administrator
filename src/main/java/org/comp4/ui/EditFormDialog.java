package org.comp4.ui;

import org.comp4.componet4.TrabajoDAO;
import org.comp4.componet4.UsuarioDAO;
import org.comp4.model.Trabajo;
import org.comp4.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditFormDialog extends JDialog {
    private JTextField endDateField, costField, clientField;
    private JTextArea problemDescriptionField, serviceDescriptionField, observationsField;
    private JComboBox<String> workTypeBox, statusBox, technicianBox;
    private JButton confirmButton, cancelButton;
    private Trabajo trabajo;
    private Runnable onEditComplete;

    public EditFormDialog(JFrame parent, Trabajo trabajo, Runnable onEditComplete) {
        super(parent, "Editar Trabajo", true);
        this.trabajo = trabajo;
        this.onEditComplete = onEditComplete;

        setLayout(new BorderLayout());
        setSize(500, 600);
        setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        workTypeBox = new JComboBox<>(new String[]{"Interno", "Externo"});
        endDateField = new JTextField(20);
        technicianBox = new JComboBox<>();
        clientField = new JTextField(20);
        problemDescriptionField = new JTextArea(3, 20);
        serviceDescriptionField = new JTextArea(3, 20);
        statusBox = new JComboBox<>(new String[]{"Pendiente", "En Progreso", "Completado", "Cancelado"});
        costField = new JTextField(20);
        observationsField = new JTextArea(3, 20);

        // Cargar datos actuales del trabajo
        workTypeBox.setSelectedItem(trabajo.getTipoTrabajo());
        endDateField.setText(trabajo.getFechaFin() != null ? new SimpleDateFormat("dd/MM/yyyy").format(trabajo.getFechaFin()) : "");
        technicianBox.setSelectedItem(trabajo.getTecnico());
        clientField.setText(trabajo.getCliente());
        problemDescriptionField.setText(trabajo.getDescripcionProblema());
        serviceDescriptionField.setText(trabajo.getDescripcionServicio());
        statusBox.setSelectedItem(trabajo.getEstado());
        costField.setText(String.valueOf(trabajo.getCosto()));
        observationsField.setText(trabajo.getObservaciones());

        addFormField(contentPanel, "Tipo de Trabajo:", workTypeBox, gbc, 0);
        addFormField(contentPanel, "Fecha de Finalización (dd/mm/yyyy):", endDateField, gbc, 1);
        addFormField(contentPanel, "Técnico Responsable:", technicianBox, gbc, 2);
        addFormField(contentPanel, "Cliente/Departamento:", clientField, gbc, 3);
        addFormField(contentPanel, "Estado de la computadora:", new JScrollPane(problemDescriptionField), gbc, 4);
        addFormField(contentPanel, "Estado de entrega:", new JScrollPane(serviceDescriptionField), gbc, 5);
        addFormField(contentPanel, "Estado del Trabajo:", statusBox, gbc, 6);
        addFormField(contentPanel, "Costos (Bs):", costField, gbc, 7);
        addFormField(contentPanel, "Observaciones:", new JScrollPane(observationsField), gbc, 8);

        JPanel buttonPanel = new JPanel();
        confirmButton = new JButton("Confirmar Edición");
        cancelButton = new JButton("Cancelar");

        confirmButton.addActionListener(e -> confirmarEdicion());
        cancelButton.addActionListener(e -> dispose());
        cargarResponsables();

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, String label, JComponent component, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void cargarResponsables() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.obtenerUsuariosDisponibles();

        technicianBox.removeAllItems(); // Limpia los elementos existentes
        technicianBox.addItem("Seleccionar Responsable"); // Añade la opción por defecto

        for (Usuario usuario : usuarios) {
            technicianBox.addItem(usuario.getNombre());
        }
    }

    private void confirmarEdicion() {
        try {
            trabajo.setTipoTrabajo((String) workTypeBox.getSelectedItem());
            trabajo.setFechaFin(endDateField.getText().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(endDateField.getText()));
            trabajo.setTecnico((String) technicianBox.getSelectedItem());
            trabajo.setCliente(clientField.getText());
            trabajo.setDescripcionProblema(problemDescriptionField.getText());
            trabajo.setDescripcionServicio(serviceDescriptionField.getText());
            trabajo.setEstado((String) statusBox.getSelectedItem());
            trabajo.setCosto(Double.parseDouble(costField.getText()));
            trabajo.setObservaciones(observationsField.getText());

            TrabajoDAO trabajoDAO = new TrabajoDAO();
            trabajoDAO.actualizarTrabajo(trabajo);

            JOptionPane.showMessageDialog(this, "Trabajo editado exitosamente.");
            dispose();
            onEditComplete.run();  // Actualizar tabla al completar edición
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar el trabajo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
