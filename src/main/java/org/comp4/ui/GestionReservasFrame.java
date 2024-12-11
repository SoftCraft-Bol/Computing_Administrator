package org.comp4.ui;

import org.comp4.MySql.DatabaseConnection;
import org.comp4.component5.LaboratorioDAO;
import org.comp4.component5.ReservaDAO;
import org.comp4.componet3.UsuarioDAO;
import org.comp4.model.Laboratorio;
import org.comp4.model.Reserva;
import org.comp4.model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GestionReservasFrame extends JFrame {
    private JTable reservasTable;
    private ReservaDAO reservaDAO;
    private LaboratorioDAO laboratorioDAO;
    private UsuarioDAO usuarioDAO;

    public GestionReservasFrame() {
        reservaDAO = new ReservaDAO();
        laboratorioDAO = new LaboratorioDAO();
        usuarioDAO = new UsuarioDAO();

        setTitle("Gestión de Reservas de Laboratorio");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel titleLabel = new JLabel("Gestión de Reservas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla de reservas
        reservasTable = new JTable();
        cargarReservas();

        JScrollPane scrollPane = new JScrollPane(reservasTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton agregarButton = new JButton("Agregar Reserva");
        agregarButton.addActionListener(e -> abrirFormularioReserva());
        buttonPanel.add(agregarButton);

        JButton editarButton = new JButton("Editar Reserva");
        editarButton.addActionListener(e -> editarReserva());
        buttonPanel.add(editarButton);

        JButton eliminarButton = new JButton("Eliminar Reserva");
        eliminarButton.addActionListener(e -> eliminarReserva());
        buttonPanel.add(eliminarButton);


        JButton cerrarButton = new JButton("Cerrar");
        cerrarButton.addActionListener(e -> dispose());
        buttonPanel.add(cerrarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void cargarReservas() {
        List<Reserva> reservas = reservaDAO.listarReservas();
        String[] columnNames = {"ID", "Laboratorio", "Usuario", "Fecha", "Hora", "Tópico"};
        Object[][] data = new Object[reservas.size()][6];

        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            String laboratorioNombre = laboratorioDAO.obtenerLaboratorioPorId(reserva.getLaboratorioId()).getNombre();
            String usuarioNombre = usuarioDAO.obtenerUsuarioPorId(reserva.getUsuarioId()).getNombre();
            data[i][0] = reserva.getId();
            data[i][1] = laboratorioNombre;
            data[i][2] = usuarioNombre;
            data[i][3] = reserva.getFecha();
            data[i][4] = reserva.getHora();
            data[i][5] = reserva.getTopico();
        }

        reservasTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void abrirFormularioReserva() {
        // Crear el panel del formulario con un GridLayout
        JPanel formularioPanel = new JPanel(new GridLayout(6, 2));

        // Crear JComboBox para seleccionar laboratorio
        JComboBox<Laboratorio> laboratorioComboBox = new JComboBox<>();
        cargarLaboratorios(laboratorioComboBox); // Método que carga los laboratorios en el comboBox

        // Crear JComboBox para seleccionar usuario
        JComboBox<Usuario> usuarioComboBox = new JComboBox<>();
        cargarUsuarios(usuarioComboBox); // Método que carga los usuarios en el comboBox

        // Crear campos de texto para fecha, hora y tópico
        JTextField fechaField = new JTextField();
        JTextField horaField = new JTextField();
        JTextField topicoField = new JTextField();

        // Agregar los campos al formulario
        formularioPanel.add(new JLabel("Laboratorio:"));
        formularioPanel.add(laboratorioComboBox);
        formularioPanel.add(new JLabel("Usuario:"));
        formularioPanel.add(usuarioComboBox);
        formularioPanel.add(new JLabel("Fecha (yyyy-mm-dd):"));
        formularioPanel.add(fechaField);
        formularioPanel.add(new JLabel("Hora:"));
        formularioPanel.add(horaField);
        formularioPanel.add(new JLabel("Tópico:"));
        formularioPanel.add(topicoField);

        // Mostrar el formulario en un cuadro de diálogo
        int option = JOptionPane.showConfirmDialog(this, formularioPanel, "Agregar Reserva", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Obtener los valores seleccionados de los JComboBox
                Laboratorio laboratorioSeleccionado = (Laboratorio) laboratorioComboBox.getSelectedItem();
                Usuario usuarioSeleccionado = (Usuario) usuarioComboBox.getSelectedItem();

                // Obtener los valores de los JTextFields
                String fecha = fechaField.getText();
                String hora = horaField.getText();
                String topico = topicoField.getText();

                // Verificar formato de la fecha
                if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "La fecha debe tener el formato yyyy-mm-dd.", "Error de formato", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear un nuevo objeto Reserva y establecer los valores
                Reserva nuevaReserva = new Reserva();
                nuevaReserva.setLaboratorioId(laboratorioSeleccionado.getId());
                nuevaReserva.setUsuarioId(usuarioSeleccionado.getId());
                nuevaReserva.setFecha(java.sql.Date.valueOf(fecha)); // Convertir la fecha de String a Date
                nuevaReserva.setHora(hora);
                nuevaReserva.setTopico(topico);

                // Intentar agregar la nueva reserva a la base de datos
                if (reservaDAO.agregarReserva(nuevaReserva)) {
                    JOptionPane.showMessageDialog(this, "Reserva agregada exitosamente.");
                    cargarReservas(); // Actualizar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error de fecha: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        }
    }
    private void editarReserva() {
        int selectedRow = reservasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva para editar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int reservaId = (int) reservasTable.getValueAt(selectedRow, 0);
        Reserva reserva = reservaDAO.obtenerReservaPorId(reservaId);

        if (reserva == null) {
            JOptionPane.showMessageDialog(this, "Error al obtener la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel formularioPanel = new JPanel(new GridLayout(6, 2));
        JComboBox<Laboratorio> laboratorioComboBox = new JComboBox<>();
        cargarLaboratorios(laboratorioComboBox);
        laboratorioComboBox.setSelectedItem(laboratorioDAO.obtenerLaboratorioPorId(reserva.getLaboratorioId()));

        JComboBox<Usuario> usuarioComboBox = new JComboBox<>();
        cargarUsuarios(usuarioComboBox);
        usuarioComboBox.setSelectedItem(usuarioDAO.obtenerUsuarioPorId(reserva.getUsuarioId()));

        JTextField fechaField = new JTextField(reserva.getFecha().toString());
        JTextField horaField = new JTextField(reserva.getHora());
        JTextField topicoField = new JTextField(reserva.getTopico());

        formularioPanel.add(new JLabel("Laboratorio:"));
        formularioPanel.add(laboratorioComboBox);
        formularioPanel.add(new JLabel("Usuario:"));
        formularioPanel.add(usuarioComboBox);
        formularioPanel.add(new JLabel("Fecha (yyyy-mm-dd):"));
        formularioPanel.add(fechaField);
        formularioPanel.add(new JLabel("Hora:"));
        formularioPanel.add(horaField);
        formularioPanel.add(new JLabel("Tópico:"));
        formularioPanel.add(topicoField);

        int option = JOptionPane.showConfirmDialog(this, formularioPanel, "Editar Reserva", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                reserva.setLaboratorioId(((Laboratorio) laboratorioComboBox.getSelectedItem()).getId());
                reserva.setUsuarioId(((Usuario) usuarioComboBox.getSelectedItem()).getId());
                reserva.setFecha(java.sql.Date.valueOf(fechaField.getText()));
                reserva.setHora(horaField.getText());
                reserva.setTopico(topicoField.getText());

                if (reservaDAO.editarReserva(reserva)) {
                    JOptionPane.showMessageDialog(this, "Reserva editada exitosamente.");
                    cargarReservas();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al editar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void eliminarReserva() {
        int selectedRow = reservasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int reservaId = (int) reservasTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar esta reserva?", "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (reservaDAO.eliminarReserva(reservaId)) {
                JOptionPane.showMessageDialog(this, "Reserva eliminada exitosamente.");
                cargarReservas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void cargarLaboratorios(JComboBox<Laboratorio> comboBox) {
        List<Laboratorio> laboratorios = laboratorioDAO.listarLaboratorios();
        for (Laboratorio laboratorio : laboratorios) {
            comboBox.addItem(laboratorio);
        }
    }

    private void cargarUsuarios(JComboBox<Usuario> comboBox) {
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();
        for (Usuario usuario : usuarios) {
            comboBox.addItem(usuario);
        }
    }
}
