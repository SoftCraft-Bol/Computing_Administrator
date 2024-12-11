package org.comp4.ui;

import org.comp4.component5.ComponenteDAO;
import org.comp4.model.Componente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GestionComponentesFrame extends JFrame {
    private JTable componentesTable;
    private ComponenteDAO componenteDAO;

    public GestionComponentesFrame() {
        setTitle("Gestión de Componentes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        componenteDAO = new ComponenteDAO();

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(45, 48, 56));

        JLabel headerLabel = new JLabel("Gestión de Componentes");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Tabla de componentes
        componentesTable = new JTable();
        actualizarTablaComponentes();

        JScrollPane scrollPane = new JScrollPane(componentesTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(45, 48, 56));

        JButton agregarButton = new JButton("Agregar");
        JButton editarButton = new JButton("Editar");
        JButton eliminarButton = new JButton("Eliminar");

        agregarButton.addActionListener(e -> agregarComponente());
        editarButton.addActionListener(e -> editarComponente());
        eliminarButton.addActionListener(e -> eliminarComponente());

        buttonPanel.add(agregarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(eliminarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void actualizarTablaComponentes() {
        try {
            List<Componente> componentes = componenteDAO.obtenerComponentes();
            String[] columnNames = {"ID", "Nombre", "Cantidad en Stock", "Precio"};
            Object[][] data = new Object[componentes.size()][4];

            for (int i = 0; i < componentes.size(); i++) {
                Componente c = componentes.get(i);
                data[i][0] = c.getId();
                data[i][1] = c.getNombre();
                data[i][2] = c.getCantidadEnStock();
                data[i][3] = c.getPrecio();
            }

            componentesTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los componentes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarComponente() {
        JTextField nombreField = new JTextField();
        JTextField cantidadField = new JTextField();
        JTextField precioField = new JTextField();

        Object[] message = {
                "Nombre:", nombreField,
                "Cantidad en Stock:", cantidadField,
                "Precio:", precioField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Agregar Componente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                Componente nuevoComponente = new Componente();
                nuevoComponente.setNombre(nombreField.getText());
                nuevoComponente.setCantidadEnStock(Integer.parseInt(cantidadField.getText()));
                nuevoComponente.setPrecio(Double.parseDouble(precioField.getText()));

                componenteDAO.agregarComponente(nuevoComponente);
                actualizarTablaComponentes();
                JOptionPane.showMessageDialog(this, "Componente agregado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el componente: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarComponente() {
        int selectedRow = componentesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un componente para editar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) componentesTable.getValueAt(selectedRow, 0);
        Componente componente = componenteDAO.obtenerComponentePorId(id);

        JTextField nombreField = new JTextField(componente.getNombre());
        JTextField cantidadField = new JTextField(String.valueOf(componente.getCantidadEnStock()));
        JTextField precioField = new JTextField(String.valueOf(componente.getPrecio()));

        Object[] message = {
                "Nombre:", nombreField,
                "Cantidad en Stock:", cantidadField,
                "Precio:", precioField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Componente", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                componente.setNombre(nombreField.getText());
                componente.setCantidadEnStock(Integer.parseInt(cantidadField.getText()));
                componente.setPrecio(Double.parseDouble(precioField.getText()));

                componenteDAO.actualizarComponente(componente);
                actualizarTablaComponentes();
                JOptionPane.showMessageDialog(this, "Componente actualizado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el componente: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarComponente() {
        int selectedRow = componentesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un componente para eliminar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) componentesTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este componente?", "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                componenteDAO.eliminarComponente(id);
                actualizarTablaComponentes();
                JOptionPane.showMessageDialog(this, "Componente eliminado exitosamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el componente: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
