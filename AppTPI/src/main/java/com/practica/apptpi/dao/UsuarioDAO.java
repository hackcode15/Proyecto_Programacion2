package com.practica.apptpi.dao;

import com.practica.apptpi.conexionBD.ConectorBD;
import com.practica.apptpi.crud.OperacionesCrudAdaptadora;
import com.practica.apptpi.modelo.Usuario;
import java.sql.*;
import java.util.*;

public class UsuarioDAO extends OperacionesCrudAdaptadora<Usuario> {

    // METODO ESPECIFICO
    // Metodo para verificar las credenciales
    public boolean verificarCredenciales(int dni, String contrasena) {
        
        String sql = "SELECT 1 FROM usuario WHERE dni = ? AND contrasena = ?";
        
        try (Connection miConexion = ConectorBD.dameConexion(); PreparedStatement miSentencia = miConexion.prepareStatement(sql)) {
            
            miSentencia.setInt(1, dni);
            miSentencia.setString(2, contrasena);
            
            try (ResultSet rs = miSentencia.executeQuery()) {

                /*if(rs.next()){
                    return true;
                }else{
                    return false;
                }*/
                return rs.next(); // true o false
                
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }
    
    // C.R.U.D
    // Create
    // Read
    // Update
    // Delete
    
    @Override
    public List<Usuario> read() {

        /*CREATE PROCEDURE listar_usuarios
        AS
        BEGIN
            SELECT
                u.dni,
                u.nombre,
                u.apellido,
                u.contrasena,
                u.correo,
                u.telefono
            FROM usuario u
            LEFT JOIN cliente c ON u.dni = c.dni
            LEFT JOIN mecanico m ON u.dni = m.dni
            WHERE c.dni IS NULL AND m.dni IS NULL;
        END*/
        String sql = "{CALL listar_usuarios}";
        
        try (Connection miConexion = ConectorBD.dameConexion(); 
                CallableStatement miSentencia = miConexion.prepareCall(sql); 
                ResultSet rs = miSentencia.executeQuery()) {
            
            List<Usuario> lista = new ArrayList<>();
            
            while (rs.next()) {
                
                Usuario usuario = Usuario.builder()
                        .dni(rs.getInt("dni"))
                        .nombre(rs.getString("nombre"))
                        .apellido(rs.getString("apellido"))
                        .contrasena(rs.getString("contrasena"))
                        .correo(rs.getString("correo"))
                        .telefono(rs.getString("telefono"))
                        .build();
                
                lista.add(usuario);
                
            }
            
            return lista;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }
    
    @Override
    public boolean delete(Usuario usuario) {
        
        String sql = "DELETE FROM usuario WHERE dni = ?";
        
        try (Connection miConexion = ConectorBD.dameConexion()) {
            
            miConexion.setAutoCommit(false);
            
            try (PreparedStatement miSentencia = miConexion.prepareStatement(sql)) {
                
                miSentencia.setInt(1, usuario.getDni());
                
                int filasAfectadas = miSentencia.executeUpdate();
                
                if (filasAfectadas == 0) {
                    throw new SQLException("Error en eliminar usuario, ninguna fila afectada");
                }
                
            } catch (SQLException e) {
                miConexion.rollback();
                System.out.println("Error al eliminar usuario, se revertio la transaccion");
            } finally {
                miConexion.setAutoCommit(true);
            }
            
            miConexion.commit();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }
    
    @Override
    public Usuario searchByDni(int dni) {

        String sql = "SELECT * FROM usuario WHERE dni = ?";
        
        try (Connection miConexion = ConectorBD.dameConexion(); PreparedStatement miSentencia = miConexion.prepareStatement(sql)) {
            
            miSentencia.setInt(1, dni);
            
            try (ResultSet rs = miSentencia.executeQuery()) {
                
                if (rs.next()) {
                    
                    Usuario usuario = Usuario.builder()
                            .dni(rs.getInt("dni"))
                            .nombre(rs.getString("nombre"))
                            .apellido(rs.getString("apellido"))
                            .contrasena(rs.getString("contrasena"))
                            .correo(rs.getString("correo"))
                            .telefono(rs.getString("telefono"))
                            .rol(rs.getString("rol"))
                            .build();
                    
                    return usuario;
                    
                } else {
                    return null;
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }
    
}
