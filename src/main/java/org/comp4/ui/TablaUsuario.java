package org.comp4.ui;

import org.comp4.model.Rol;
import org.comp4.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TablaUsuario extends JFrame {

    public TablaUsuario(Usuario usuario) {
        setTitle("Panel de Usuario - " + usuario.getNombre());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Roles de Usuario:");
        add(label, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(0, 1));

        for (Rol rol : usuario.getRoles()) {
            JButton boton = new JButton("Acceso: " + rol.getNombre());
            boton.addActionListener(e -> manejarAccionRol(rol));
            panelBotones.add(boton);
        }

        add(panelBotones, BorderLayout.CENTER);
    }

    public TablaUsuario() {

    }

    private void manejarAccionRol(Rol rol) {
        JOptionPane.showMessageDialog(this, "Función: " + rol.getNombre());
    }
}

