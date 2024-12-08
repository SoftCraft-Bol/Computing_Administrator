package org.comp5.interfaz;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReservaLaboratorioForm extends JFrame {
    private JDateChooser dateField;
    private JSpinner timeField;
    private JTextField durationField;
    private JTextArea purposeField;
    private JComboBox<String> labBox, statusBox;
    private JButton submitButton, resetButton, backButton;
    private String rolSeleccionado;
    private String usuarioSeleccionado;

    public ReservaLaboratorioForm(String rolSeleccionado, String usuarioSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
        this.usuarioSeleccionado = usuarioSeleccionado;
        setTitle("Gestión de Reservas de Laboratorio");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Crear panel superior para mostrar rol y usuario
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel rolLabel = new JLabel("Rol Seleccionado: " + rolSeleccionado);
        rolLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rolLabel.setForeground(new Color(25, 25, 112));

        JLabel usuarioLabel = new JLabel("Usuario Seleccionado: " + usuarioSeleccionado);
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usuarioLabel.setForeground(new Color(25, 25, 112));

        topPanel.add(rolLabel);
        topPanel.add(Box.createVerticalStrut(5)); // Espaciado entre etiquetas
        topPanel.add(usuarioLabel);

        add(topPanel, BorderLayout.NORTH);

        // Crear las pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(29, 147, 147));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));

        tabbedPane.addTab("Nueva Reserva", createReservationForm());
        tabbedPane.addTab("Reservas Activas", createActiveReservationsTable());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createReservationForm() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(15, 16, 16));
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(243, 250, 252));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        labBox = new JComboBox<>(new String[]{"Lab 1", "Lab 2", "Lab 3", "Laboratorio de Redes"});

        dateField = new JDateChooser();
        dateField.setDateFormatString("dd/MM/yyyy");

        // JSpinner para la hora
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeField = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeField, "HH:mm");
        timeField.setEditor(timeEditor);

        durationField = new JTextField(20);
        //userField = new JTextField(20);
        purposeField = new JTextArea(3, 20);
        statusBox = new JComboBox<>(new String[]{"Pendiente", "Aprobada", "Rechazada"});

        int row = 0;
        addFormField(contentPanel, "Laboratorio:", labBox, gbc, row++);
        addFormField(contentPanel, "Fecha (dd/mm/yyyy):", dateField, gbc, row++);
        addFormField(contentPanel, "Hora (hh:mm):", timeField, gbc, row++);
        addFormField(contentPanel, "Duración (minutos):", durationField, gbc, row++);
        //addFormField(contentPanel, "Usuario:", userField, gbc, row++);
        addFormField(contentPanel, "Propósito:", new JScrollPane(purposeField), gbc, row++);
        addFormField(contentPanel, "Estado:", statusBox, gbc, row++);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(216, 236, 243));
        submitButton = crearBoton("Registrar Reserva", new Color(60, 179, 113), Color.WHITE);
        resetButton = crearBoton("Reiniciar", new Color(255, 99, 71), Color.WHITE);
        backButton = crearBoton("Volver", new Color(30, 144, 255), Color.WHITE);

        submitButton.addActionListener(e -> registrarReserva());
        resetButton.addActionListener(e -> reiniciarFormulario());
        backButton.addActionListener(e -> volverAction());

        buttonPanel.add(submitButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createActiveReservationsTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        String[] columnNames = {"ID", "Laboratorio", "Fecha", "Hora", "Duración", "Usuario", "Estado", "Propósito"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        aplicarEstiloTabla(table);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones de acción
        JButton deleteButton = crearBoton("Eliminar", new Color(220, 20, 60), Color.WHITE);
        JButton editButton = crearBoton("Editar", new Color(30, 144, 255), Color.WHITE);


        deleteButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Eliminar reserva no implementado."));
        editButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Editar reserva no implementado."));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
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

    private void aplicarEstiloTabla(JTable table) {
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setBackground(new Color(240, 248, 255));
        table.setGridColor(new Color(173, 216, 230));
    }

    private void registrarReserva() {
        try {
            String laboratorio = (String) labBox.getSelectedItem();
            Date selectedDate = dateField.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fecha.");
                return;
            }
            String fecha = new SimpleDateFormat("dd/MM/yyyy").format(selectedDate);
            Date selectedTime = (Date) timeField.getValue();
            String hora = new SimpleDateFormat("HH:mm").format(selectedTime);
            int duracion = Integer.parseInt(durationField.getText());
            //String usuario = userField.getText();
            String proposito = purposeField.getText();

            JOptionPane.showMessageDialog(this, "Reserva registrada:\n"
                    + "Laboratorio: " + laboratorio + "\n"
                    + "Fecha: " + fecha + "\n"
                    + "Hora: " + hora + "\n"
                    + "Duración: " + duracion + " minutos\n"
                    + "Propósito: " + proposito, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            reiniciarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void volverAction(){
        SeleccionDeRol seleccionDeRol = new SeleccionDeRol();
        seleccionDeRol.setVisible(true);
        dispose();
    }

    private void reiniciarFormulario() {
        labBox.setSelectedIndex(0);
        dateField.setDate(null);
        timeField.setValue(new Date());
        durationField.setText("");
        purposeField.setText("");
        statusBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReservaLaboratorioForm form = new ReservaLaboratorioForm("Administrador", "Alfredo");
            form.setVisible(true);
        });
    }
}