
// Paquete para la UI y la visualización de los datos
package org.comp4.ui;

import org.comp4.componet4.TrabajoDAO;
import org.comp4.componet3.UsuarioDAO;
import org.comp4.model.Trabajo;
import org.comp4.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class TablaMantenimiento extends JFrame {
    private JTextField endDateField, costField, clientField;
    private JTextArea problemDescriptionField, serviceDescriptionField, observationsField;
    private JComboBox<String> workTypeBox, statusBox, technicianBox;
    private JButton submitButton, resetButton, editButton, confirmEditButton;
    private JTable activeJobsTable;
    private DefaultTableModel model;
    private boolean isEditing = false;
    private int editingTrabajoId = -1;

    public TablaMantenimiento() {
        setTitle("Sistema de Seguimiento de Mantenimiento");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear las pestañas para las funcionalidades
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(29, 147, 147));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));

        // Pestañas para el formulario y la tabla
        tabbedPane.addTab("Registrar/Editar Trabajo", createMaintenanceForm());
        tabbedPane.addTab("Trabajos Activos", createActiveJobsTable());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createMaintenanceForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 16, 16));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(243, 250, 252));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Crear campos del formulario
        workTypeBox = new JComboBox<>(new String[]{"Interno", "Externo"});
        endDateField = new JTextField(20);
        technicianBox = new JComboBox<>();
        clientField = new JTextField(20);
        problemDescriptionField = new JTextArea(3, 20);
        serviceDescriptionField = new JTextArea(3, 20);
        statusBox = new JComboBox<>(new String[]{"Pendiente", "En Progreso", "Completado", "Cancelado"});
        costField = new JTextField(20);
        observationsField = new JTextArea(3, 20);

        int row = 0;
        addFormField(contentPanel, "Tipo de Trabajo:", workTypeBox, gbc, row++);
        addFormField(contentPanel, "Fecha de Finalización (dd/mm/yyyy):", endDateField, gbc, row++);
        addFormField(contentPanel, "Técnico Responsable:", technicianBox, gbc, row++);
        addFormField(contentPanel, "Cliente/Departamento:", clientField, gbc, row++);
        addFormField(contentPanel, "Descripción del Problema:", new JScrollPane(problemDescriptionField), gbc, row++);
        addFormField(contentPanel, "Descripción del Servicio:", new JScrollPane(serviceDescriptionField), gbc, row++);
        addFormField(contentPanel, "Estado del Trabajo:", statusBox, gbc, row++);
        addFormField(contentPanel, "Costos (Bs):", costField, gbc, row++);
        addFormField(contentPanel, "Observaciones:", new JScrollPane(observationsField), gbc, row++);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(216, 236, 243));
        submitButton = new JButton("Registrar Trabajo");
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        resetButton = new JButton("Reiniciar");
        resetButton.setBackground(new Color(255, 99, 71));
        resetButton.setForeground(Color.WHITE);
        confirmEditButton = new JButton("Confirmar Edición");
        confirmEditButton.setBackground(new Color(255, 165, 0));
        confirmEditButton.setForeground(Color.WHITE);
        confirmEditButton.setVisible(false);

        submitButton.addActionListener(e -> registrarTrabajo());
        resetButton.addActionListener(e -> reiniciarFormulario());
        confirmEditButton.addActionListener(e -> confirmarEdicionTrabajo());

        buttonPanel.add(submitButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(confirmEditButton);

        // Cargar técnicos disponibles
        cargarResponsables();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
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


    private void registrarTrabajo() {
        if (isEditing) {
            JOptionPane.showMessageDialog(this, "Está en modo edición. Confirme la edición o cancele antes de registrar un nuevo trabajo.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Trabajo trabajo = new Trabajo();
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
            trabajoDAO.insertarTrabajo(trabajo);

            JOptionPane.showMessageDialog(this, "Trabajo registrado exitosamente.");
            reiniciarFormulario();
            actualizarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el trabajo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void editarTrabajoSeleccionado() {
        int selectedRow = activeJobsTable.getSelectedRow();

        if (selectedRow != -1) {
            int trabajoId = (int) activeJobsTable.getValueAt(selectedRow, 0);
            TrabajoDAO trabajoDAO = new TrabajoDAO();
            Trabajo trabajo = trabajoDAO.obtenerTrabajoPorId(trabajoId);

            if (trabajo != null) {
                EditFormDialog editDialog = new EditFormDialog(this, trabajo, this::actualizarTabla);
                editDialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un trabajo para editar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void confirmarEdicionTrabajo() {
        if (!isEditing || editingTrabajoId == -1) {
            JOptionPane.showMessageDialog(this, "No hay trabajo seleccionado para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Trabajo trabajo = new Trabajo();
            trabajo.setId(editingTrabajoId);
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
            reiniciarFormulario();
            actualizarTabla();

            isEditing = false;
            editingTrabajoId = -1;
            submitButton.setVisible(true);
            confirmEditButton.setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar el trabajo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void reiniciarFormulario() {
        endDateField.setText("");
        technicianBox.setSelectedIndex(0);
        clientField.setText("");
        problemDescriptionField.setText("");
        serviceDescriptionField.setText("");
        costField.setText("");
        observationsField.setText("");
        workTypeBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        isEditing = false;
        editingTrabajoId = -1;
        submitButton.setVisible(true);
        confirmEditButton.setVisible(false);
    }

    private JPanel createActiveJobsTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        // Nombres de las columnas, incluyendo la columna para el nombre del técnico
        String[] columnNames = {"ID", "Fecha de Fin", "Técnico", "Estado", "Costo", "Cliente", "Descripción Problema", "Descripción Servicio"};
        model = new DefaultTableModel(columnNames, 0);
        activeJobsTable = new JTable(model);
        styleTable(activeJobsTable);

        JScrollPane scrollPane = new JScrollPane(activeJobsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón de Eliminar
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> eliminarTrabajoSeleccionado());

        // Botón de Editar
        editButton = new JButton("Editar");
        editButton.setBackground(new Color(30, 144, 255));
        editButton.setForeground(Color.WHITE);
        editButton.addActionListener(e -> editarTrabajoSeleccionado());

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Actualizar la tabla con los trabajos
        actualizarTabla();
        return panel;
    }


    private void eliminarTrabajoSeleccionado() {
        int selectedRow = activeJobsTable.getSelectedRow();

        if (selectedRow != -1) { // Verifica que haya una fila seleccionada
            int trabajoId = (int) activeJobsTable.getValueAt(selectedRow, 0); // Obtiene el ID del trabajo seleccionado

            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este trabajo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                TrabajoDAO trabajoDAO = new TrabajoDAO();
                trabajoDAO.eliminarTrabajo(trabajoId); // Elimina el trabajo de la base de datos
                actualizarTabla(); // Actualiza la tabla para reflejar los cambios
                JOptionPane.showMessageDialog(this, "Trabajo eliminado exitosamente.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un trabajo para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarTabla() {
        model.setRowCount(0);

        TrabajoDAO trabajoDAO = new TrabajoDAO();
        List<Trabajo> trabajos = trabajoDAO.obtenerTodosLosTrabajos();
        for (Trabajo trabajo : trabajos) {
            model.addRow(new Object[] {
                    trabajo.getId(),
                    trabajo.getFechaFin(),
                    trabajo.getTecnico(), // Nombre del técnico (ya debería estar correctamente recuperado)
                    trabajo.getEstado(),
                    trabajo.getCosto(),
                    trabajo.getCliente(),
                    trabajo.getDescripcionProblema(),
                    trabajo.getDescripcionServicio()
            });
        }

    }

    private void styleTable(JTable table) {
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setBackground(new Color(240, 248, 255));
        table.setGridColor(new Color(173, 216, 230));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TablaMantenimiento frame = new TablaMantenimiento();
            frame.setVisible(true);
        });


    }
}