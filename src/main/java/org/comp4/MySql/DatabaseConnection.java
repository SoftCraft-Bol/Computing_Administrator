package org.comp4.MySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_mantenimiento2";
    private static final String USER = "gaspar";
    private static final String PASSWORD = "armando1gaspar";


    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión a MySQL exitosa.");
        } catch (SQLException e) {
            System.out.println("Error en la conexión a MySQL: " + e.getMessage());
        }
        return connection;
    }

}
