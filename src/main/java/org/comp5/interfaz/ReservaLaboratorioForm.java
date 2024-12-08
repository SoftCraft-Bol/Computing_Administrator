package org.comp5.interfaz;

import com.toedter.calendar.JDateChooser;
import org.comp5.controllador.DataBase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReservaLaboratorioForm extends JFrame {
    private JDateChooser dateField;
    private JSpinner timeField;
    private JSpinner durationField;
    private JTextArea purposeField;
    private JComboBox<String> labBox;
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
        cargarLaboratorios();
    }

    private void cargarLaboratorios() {
        DataBase db = new DataBase();
        try {
            List<String> laboratorios = db.obtenerLaboratorios();
            for (String laboratorio : laboratorios) {
                labBox.addItem(laboratorio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar laboratorios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        labBox = new JComboBox<>();

        dateField = new JDateChooser();
        dateField.setDateFormatString("dd/MM/yyyy");

        // JSpinner para la hora
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeField = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeField, "HH:mm");
        timeField.setEditor(timeEditor);

        // JSpinner para la duración con intervalos de 90 minutos
        SpinnerDateModel durationModel = new SpinnerDateModel();
        durationField = new JSpinner(durationModel);
        JSpinner.DateEditor durationEditor = new JSpinner.DateEditor(durationField, "HH:mm") {
            public void setValue(Object value) {
                Calendar cal = Calendar.getInstance();
                cal.setTime((Date) value);
                int minutes = cal.get(Calendar.MINUTE);
                if (minutes % 30 != 0) {
                    cal.set(Calendar.MINUTE, (minutes / 30) * 30);
                }
                getModel().setValue(cal.getTime());
            }
        };
        durationField.setEditor(durationEditor);

        // Establecer el valor inicial y los intervalos de 90 minutos
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 30);
        durationField.setValue(calendar.getTime());

        purposeField = new JTextArea(3, 20);

        int row = 0;
        addFormField(contentPanel, "Laboratorio:", labBox, gbc, row++);
        addFormField(contentPanel, "Fecha (dd/MM/yyyy):", dateField, gbc, row++);
        addFormField(contentPanel, "Hora (HH:mm):", timeField, gbc, row++);
        addFormField(contentPanel, "Duración (HH:mm):", durationField, gbc, row++);
        addFormField(contentPanel, "Propósito:", new JScrollPane(purposeField), gbc, row++);

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

        String[] columnNames = {"ID", "Laboratorio", "Fecha", "Hora", "Duración", "Usuario", "Propósito"};
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

        cargarReservasActivas(model);

        return panel;
    }
    private void cargarReservasActivas(DefaultTableModel model) {
        DataBase db = new DataBase();
        try {
            List<Object[]> reservas = db.obtenerReservasActivas();
            for (Object[] reserva : reservas) {
                model.addRow(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar reservas activas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            String fecha = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
            Date selectedTime = (Date) timeField.getValue();
            String hora = new SimpleDateFormat("HH:mm:ss").format(selectedTime);
            Date selectedDuration = (Date) durationField.getValue();
            String duracion = new SimpleDateFormat("HH:mm:ss").format(selectedDuration);
            String proposito = purposeField.getText();

            DataBase db = new DataBase();
            int laboratorioId = db.obtenerLaboratorioIdPorNombre(laboratorio);
            int usuarioId = db.obtenerUsuarioIdPorNombre(usuarioSeleccionado);

            db.registrarReserva(laboratorioId, usuarioId, java.sql.Date.valueOf(fecha), java.sql.Time.valueOf(hora), java.sql.Time.valueOf(duracion), proposito);

            JOptionPane.showMessageDialog(this, "Reserva registrada:\n"
                    + "Laboratorio: " + laboratorio + "\n"
                    + "Fecha: " + fecha + "\n"
                    + "Hora: " + hora + "\n"
                    + "Duración: " + duracion + " hrs\n"
                    + "Propósito: " + proposito, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            reiniciarFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void volverAction() {
        SeleccionDeRol seleccionDeRol = new SeleccionDeRol();
        seleccionDeRol.setVisible(true);
        dispose();
    }

    private void reiniciarFormulario() {
        labBox.setSelectedIndex(0);
        dateField.setDate(null);
        timeField.setValue(new Date());
        durationField.setValue(new Date());
        purposeField.setText("");
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            ReservaLaboratorioForm form = new ReservaLaboratorioForm("Administrador", "Alfredo");
//            form.setVisible(true);
//        });
//    }
}