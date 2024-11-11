package com.practica.apptpi.conexionBD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConectorBD {
    private static String DB_URL = "";
    private static String USER = "";
    private static String PASS = "";
    
    public static String[] dameDatos() {
        
        String ruta = "D:/Documents/Proyecto_Programacion2/AppTPI/src/main/java/com/practica/apptpi/conexionBD/credenciales.txt";
        //String ruta = "D:/Documents/NetBeansProjects/AppTPI/src/main/java/com/practica/apptpi/conexionBD/credenciales.txt";
        
        String datos[] = new String[3];
        
        try (FileReader archivo = new FileReader(ruta);
             BufferedReader miBuffer = new BufferedReader(archivo)) {
            
            String linea;
            
            for (int i = 0; i < datos.length; i++) {
                linea = miBuffer.readLine();
                
                if (linea != null) {
                    datos[i] = linea;
                } else {
                    break;
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return datos;
    }
    
    public static Connection dameConexion() throws SQLException {
        try {
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String datos[] = dameDatos();
            
            DB_URL = datos[0];
            USER = datos[1];
            PASS = datos[2];
            
            Properties connectionProps = new Properties();
            connectionProps.setProperty("user", USER);
            connectionProps.setProperty("password", PASS);
            connectionProps.setProperty("encrypt", "true");
            connectionProps.setProperty("trustServerCertificate", "true");
            
            return DriverManager.getConnection(DB_URL, connectionProps);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el controlador JDBC: " + e.getMessage());
        }
    }
}
