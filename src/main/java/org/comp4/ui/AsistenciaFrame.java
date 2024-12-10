package org.comp4.ui;

import org.comp4.componet1.AsistenciaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AsistenciaFrame extends JFrame {
    private int usuarioId; // ID del usuario autenticado.
    private JTextField observacionesField; // Campo para observaciones
    private DefaultTableModel tableModel; // Modelo para la tabla

    public AsistenciaFrame(int usuarioId) {
        this.usuarioId = usuarioId;

        setTitle("Registro de Asistencia");
        setSize(500, 600); // Ajustado para más espacio
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(40, 44, 52));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Seleccione una acción:");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(label);

        mainPanel.add(Box.createVerticalStrut(15));

        observacionesField = new JTextField();
        observacionesField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        observacionesField.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        mainPanel.add(observacionesField);

        mainPanel.add(Box.createVerticalStrut(15));

        JButton registrarEntradaButton = new JButton("Registrar Entrada");
        registrarEntradaButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarEntradaButton.setBackground(new Color(76, 175, 80));
        registrarEntradaButton.setForeground(Color.WHITE);
        registrarEntradaButton.addActionListener(e -> {
            registrarEntrada();
            cargarAsistencias(); // Actualizar tabla después de registrar entrada
        });

        JButton registrarSalidaButton = new JButton("Registrar Salida");
        registrarSalidaButton.setFont(new Font("Arial", Font.BOLD, 14));
        registrarSalidaButton.setBackground(new Color(244, 67, 54));
        registrarSalidaButton.setForeground(Color.WHITE);
        registrarSalidaButton.addActionListener(e -> {
            registrarSalida();
            cargarAsistencias(); // Actualizar tabla después de registrar salida
        });

        mainPanel.add(registrarEntradaButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(registrarSalidaButton);

        mainPanel.add(Box.createVerticalStrut(20));

        // Tabla para mostrar asistencias
        String[] columnNames = {"Fecha", "Entrada", "Salida", "Estado", "Observaciones"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable asistenciaTable = new JTable(tableModel);
        asistenciaTable.setEnabled(false); // Hacer que la tabla no sea editable
        asistenciaTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(asistenciaTable);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        mainPanel.add(scrollPane);

        add(mainPanel);

        // Cargar asistencias al iniciar la interfaz
        cargarAsistencias();
    }

    private void registrarEntrada() {
        try {
            String observaciones = observacionesField.getText().trim();
            AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
            asistenciaDAO.registrarEntrada(usuarioId, observaciones);
            JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar entrada: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarSalida() {
        try {
            String observaciones = observacionesField.getText().trim();
            AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
            asistenciaDAO.registrarSalida(usuarioId, observaciones);
            JOptionPane.showMessageDialog(this, "Salida registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar salida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarAsistencias() {
        try {
            AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
            List<String[]> asistencias = asistenciaDAO.obtenerAsistenciasParaTabla(usuarioId);
            tableModel.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

            for (String[] asistencia : asistencias) {
                tableModel.addRow(asistencia);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las asistencias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
