package org.comp5;

import org.comp5.interfaz.SeleccionDeRol;

import javax.swing.*;

public class Run {
    public static void main(String[] args) {
        System.out.println("Corriendo el modulo 5");
        SwingUtilities.invokeLater(() -> {
            SeleccionDeRol seleccionDeRol = new SeleccionDeRol();
            seleccionDeRol.setVisible(true);
        });
    }
}
