package com.practica.apptpi.dao;

import com.practica.apptpi.conexionBD.ConectorBD;
import com.practica.apptpi.crud.OperacionesCrud;
import com.practica.apptpi.modelo.Mecanico;
import java.sql.*;
import java.util.*;

public class MecanicoDAO implements OperacionesCrud<Mecanico>{

    @Override
    public boolean create(Mecanico mecanico) {
        
        /*CREATE PROCEDURE agregar_mecanico
                @dni BIGINT,
                @nombre VARCHAR(100),
                @apellido VARCHAR(100),
                @contrasena VARCHAR(100),
                @correo VARCHAR(100),
                @telefono VARCHAR(100),
                @rol VARCHAR(20),
                @sueldo DECIMAL(10, 2)
        AS
        BEGIN
                INSERT INTO usuario(dni, nombre, apellido, contrasena, correo, telefono, rol)
                VALUES(@dni, @nombre, @apellido, @contrasena, @correo, @telefono, @rol);
                INSERT INTO mecanico(dni, sueldo)
                VALUES(@dni, @sueldo);
        END*/
        
        String sql = "{CALL agregar_mecanico(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            try(CallableStatement miSentencia = miConexion.prepareCall(sql)){
                
                miSentencia.setInt(1, mecanico.getDni());
                miSentencia.setString(2, mecanico.getNombre());
                
                if(mecanico.getApellido() != null && !mecanico.getApellido().isEmpty()){
                    miSentencia.setString(3, mecanico.getApellido());
                }else{
                    miSentencia.setNull(3, java.sql.Types.VARCHAR);
                }
                
                miSentencia.setString(4, mecanico.getContrasena());
                miSentencia.setString(5, mecanico.getCorreo());
                
                if(mecanico.getTelefono() != null && !mecanico.getTelefono().isEmpty()){
                    miSentencia.setString(6, mecanico.getTelefono());
                }else{
                    miSentencia.setNull(6, java.sql.Types.VARCHAR);
                }
                
                miSentencia.setString(7, mecanico.getRol());
                miSentencia.setDouble(8, mecanico.getSueldo());
                
                
                int filasAfectadas = miSentencia.executeUpdate();
                
                if(filasAfectadas == 0){
                    throw new SQLException("Error en agregar administrador, ninguna fila afectada");
                } 
                
            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error al agregar administrador, se revertio la transaccion");
                return false;
            }finally{
                miConexion.setAutoCommit(true);
            }
            
            miConexion.commit();
            return true;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public List<Mecanico> read() {
        
        /*CREATE PROCEDURE listar_mecanicos
        AS
        BEGIN
            SELECT 
                u.dni,
                u.nombre,
                u.apellido,
                u.telefono,
                m.fechaIngreso,
                m.sueldo
            FROM mecanico m
            LEFT JOIN usuario u ON m.dni = u.dni
        END*/
        
        String sql = "{CALL listar_mecanicos}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql);
                ResultSet rs = miSentencia.executeQuery()){
            
            List<Mecanico> lista = new ArrayList<>();
            
            while(rs.next()){
                
                Mecanico mecanico = Mecanico.builder()
                        .dni(rs.getInt("dni"))
                        .nombre(rs.getString("nombre"))
                        .apellido(rs.getString("apellido"))
                        .telefono(rs.getString("telefono"))
                        .fechaIngreso(rs.getDate("fechaIngreso"))
                        .sueldo(rs.getDouble("sueldo"))
                        .build();
                
                lista.add(mecanico);
                
            }
            
            return lista;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }

    @Override
    public boolean update(Mecanico mecanico) {
    
        /*CREATE PROCEDURE actualizar_mecanico
            @dni BIGINT,
            @contrasena VARCHAR(100),
            @telefono VARCHAR(100),
            @correo VARCHAR(100),
            @sueldo DECIMAL(10, 2)
        AS
        BEGIN
            UPDATE usuario SET contrasena = @contrasena, telefono = @telefono, correo = @correo WHERE dni = @dni;
            UPDATE mecanico SET sueldo = @sueldo WHERE dni = @dni;
        END*/
        
        String sql = "{CALL actualizar_mecanico(?, ?, ?, ?, ?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            try(CallableStatement miSentencia = miConexion.prepareCall(sql)){
                
                miSentencia.setInt(1, mecanico.getDni());
                miSentencia.setString(2, mecanico.getContrasena());
                miSentencia.setString(3, mecanico.getTelefono());
                miSentencia.setString(4, mecanico.getCorreo());
                miSentencia.setDouble(5, mecanico.getSueldo());
                
                int filasAfectadas = miSentencia.executeUpdate();
                
                if(filasAfectadas == 0){
                    throw new SQLException("Error en actualizar administrador, ninguna fila afectada");
                }
                
            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error en actualizar administrador, se revertio la operacion");
                return false;
            }finally{
                miConexion.setAutoCommit(true);
            }
         
            miConexion.commit();
            return true;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public boolean delete(Mecanico mecanico) {
    
        String sql = "DELETE FROM mecanico WHERE dni = ?";
    
        try(Connection miConexion = ConectorBD.dameConexion();
                PreparedStatement miSentencia = miConexion.prepareStatement(sql)){
            
            miSentencia.setInt(1, mecanico.getDni());
            
            int filasAfectadas = miSentencia.executeUpdate();
            
            return filasAfectadas > 0;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    
    }

    @Override
    public Mecanico searchByDni(int dni) {
        
        /*CREATE PROCEDURE buscar_mecanico
            @dni BIGINT
        AS
        BEGIN
            SELECT 
                u.dni,
                u.nombre,
                u.apellido,
                u.contrasena,
                u.correo,
                u.telefono,
                m.fechaIngreso,
                m.sueldo
            FROM mecanico m
            LEFT JOIN usuario u ON m.dni = u.dni
            WHERE m.dni = @dni
        END*/
        
        String sql = "{CALL buscar_mecanico(?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql)){
            
            miSentencia.setInt(1, dni);
            
            try(ResultSet rs = miSentencia.executeQuery()){
                
                if(rs.next()){
                    
                    Mecanico mecanico = Mecanico.builder()
                            .dni(rs.getInt("dni"))
                            .nombre(rs.getString("nombre"))
                            .apellido(rs.getString("apellido"))
                            .contrasena(rs.getString("contrasena"))
                            .correo(rs.getString("correo"))
                            .telefono(rs.getString("telefono"))
                            .fechaIngreso(rs.getDate("fechaIngreso"))
                            .sueldo(rs.getDouble("sueldo"))
                            .build();
                    
                    return mecanico;
                    
                }else{
                    return null;
                }
                
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }
   
}
