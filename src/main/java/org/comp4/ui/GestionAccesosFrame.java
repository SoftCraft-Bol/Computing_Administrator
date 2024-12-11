package org.comp4.ui;

import org.comp4.componet1.AccesoLaboratorioDAO;
import org.comp4.componet3.UsuarioDAO;
import org.comp4.component5.LaboratorioDAO;
import org.comp4.model.AccesoLaboratorio;
import org.comp4.model.Laboratorio;
import org.comp4.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GestionAccesosFrame extends JFrame {
    private JTable accesosTable;
    private AccesoLaboratorioDAO accesoLaboratorioDAO;
    private LaboratorioDAO laboratorioDAO;
    private UsuarioDAO usuarioDAO;

    public GestionAccesosFrame() {
        setTitle("Gestión de Accesos a Laboratorios");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        accesoLaboratorioDAO = new AccesoLaboratorioDAO();
        laboratorioDAO = new LaboratorioDAO();
        usuarioDAO = new UsuarioDAO();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Gestión de Accesos a Laboratorios", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        accesosTable = new JTable();
        actualizarTablaAccesos();
        JScrollPane scrollPane = new JScrollPane(accesosTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton registrarButton = new JButton("Registrar");
        registrarButton.addActionListener(e -> registrarAcceso());

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.addActionListener(e -> eliminarAcceso());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registrarButton);
        buttonPanel.add(eliminarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void actualizarTablaAccesos() {
        List<AccesoLaboratorio> accesos = accesoLaboratorioDAO.obtenerAccesos();
        String[] columnNames = {"ID", "Laboratorio", "Usuario", "Fecha", "Hora Entrada", "Hora Salida", "Motivo"};
        Object[][] data = new Object[accesos.size()][7];

        for (int i = 0; i < accesos.size(); i++) {
            AccesoLaboratorio a = accesos.get(i);
            String laboratorioNombre = laboratorioDAO.obtenerLaboratorioPorId(a.getLaboratorioId()).getNombre();
            String usuarioNombre = usuarioDAO.obtenerUsuarioPorId(a.getUsuarioId()).getNombre();
            data[i][0] = a.getId();
            data[i][1] = laboratorioNombre;
            data[i][2] = usuarioNombre;
            data[i][3] = a.getFechaAcceso();
            data[i][4] = a.getHoraEntrada();
            data[i][5] = a.getHoraSalida();
            data[i][6] = a.getMotivoAcceso();
        }

        accesosTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void registrarAcceso() {
        JComboBox<Laboratorio> laboratorioComboBox = new JComboBox<>();
        cargarLaboratorios(laboratorioComboBox);

        JComboBox<Usuario> usuarioComboBox = new JComboBox<>();
        cargarUsuarios(usuarioComboBox);

        JTextField fechaField = new JTextField();
        JTextField horaEntradaField = new JTextField();
        JTextField horaSalidaField = new JTextField();
        JTextArea motivoField = new JTextArea();

        Object[] message = {
                "Laboratorio:", laboratorioComboBox,
                "Usuario:", usuarioComboBox,
                "Fecha (YYYY-MM-DD):", fechaField,
                "Hora de Entrada (HH:MM:SS):", horaEntradaField,
                "Hora de Salida (HH:MM:SS):", horaSalidaField,
                "Motivo:", new JScrollPane(motivoField)
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Acceso", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Laboratorio laboratorioSeleccionado = (Laboratorio) laboratorioComboBox.getSelectedItem();
                Usuario usuarioSeleccionado = (Usuario) usuarioComboBox.getSelectedItem();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                Date parsedDate = dateFormat.parse(fechaField.getText().trim());
                java.sql.Date fecha = new java.sql.Date(parsedDate.getTime());

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                timeFormat.setLenient(false);
                Time horaEntrada = new Time(timeFormat.parse(horaEntradaField.getText().trim()).getTime());
                Time horaSalida = new Time(timeFormat.parse(horaSalidaField.getText().trim()).getTime());

                boolean tieneReserva = accesoLaboratorioDAO.verificarReserva(laboratorioSeleccionado.getId(), usuarioSeleccionado.getId(), fecha);

                if (!tieneReserva) {
                    JOptionPane.showMessageDialog(this, "El usuario no tiene una reserva activa para este laboratorio y fecha.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String motivo = motivoField.getText().trim();

                AccesoLaboratorio acceso = new AccesoLaboratorio();
                acceso.setLaboratorioId(laboratorioSeleccionado.getId());
                acceso.setUsuarioId(usuarioSeleccionado.getId());
                acceso.setFechaAcceso(fecha);
                acceso.setHoraEntrada(horaEntrada);
                acceso.setHoraSalida(horaSalida);
                acceso.setMotivoAcceso(motivo);

                accesoLaboratorioDAO.registrarAcceso(acceso);
                actualizarTablaAccesos();
                JOptionPane.showMessageDialog(this, "Acceso registrado correctamente.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en los campos correspondientes.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha u hora incorrecto. Por favor, use los formatos YYYY-MM-DD y HH:MM:SS.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar el acceso: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarAcceso() {
        int selectedRow = accesosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un acceso para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int accesoId = (int) accesosTable.getValueAt(selectedRow, 0);
        accesoLaboratorioDAO.eliminarAcceso(accesoId);
        actualizarTablaAccesos();
        JOptionPane.showMessageDialog(this, "Acceso eliminado correctamente.");
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
