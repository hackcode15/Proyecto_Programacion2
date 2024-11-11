package com.practica.apptpi.dao;

import com.practica.apptpi.conexionBD.ConectorBD;
import com.practica.apptpi.crud.OperacionesCrud;
import com.practica.apptpi.modelo.Cliente;
import java.sql.*;
import java.util.*;

// TERMINADO
public class ClienteDAO implements OperacionesCrud<Cliente> {

    // M.V.C (Modelo Vista Controlador)
    
    // GENERACION DEL CRUD BASICO
    
    // CREATE LISTO
    @Override
    public boolean create(Cliente cliente) {

        // Procedimiento almacenado - Codigo SQL Server
        /*CREATE PROCEDURE agregar_cliente
                @dni BIGINT,
                @nombre VARCHAR(100),
                @apellido VARCHAR(45),
                @contrasena VARCHAR(100),
                @correo VARCHAR(100),
                @telefono VARCHAR(100),
                @domicilio VARCHAR(100),
                @RegimenLaboral VARCHAR(100),
                @rol VARCHAR(20)
        AS
        BEGIN
                INSERT INTO usuario(dni, nombre, apellido, contrasena, correo, telefono, rol)
                VALUES(@dni, @nombre, @apellido, @contrasena, @correo, @telefono, @rol);
                INSERT INTO cliente(dni, domicilio, RegimenLaboral)
                VALUES(@dni, @domicilio, @RegimenLaboral);
        END*/
        

        String sql = "{CALL agregar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            try(CallableStatement miSentencia = miConexion.prepareCall(sql)){
                
                // Manejo de excepciones 
                if (cliente.getDni() <= 0) { 
                    throw new SQLException("El DNI es obligatorio");
                }
                if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) { 
                    throw new SQLException("El nombre es obligatorio"); 
                }
                if (cliente.getContrasena()== null || cliente.getContrasena().isEmpty()) { 
                    throw new SQLException("La contraseña es obligatorio"); 
                }
                if (cliente.getCorreo()== null || cliente.getCorreo().isEmpty()) { 
                    throw new SQLException("El correo es obligatorio"); 
                }
                if (cliente.getRegimenLaboral()== null || cliente.getRegimenLaboral().isEmpty()) { 
                    throw new SQLException("El tipo de regimen laboral es obligatorio"); 
                }
                
                miSentencia.setInt(1, cliente.getDni());
                miSentencia.setString(2, cliente.getNombre());
                
                // campo opcional
                if(cliente.getApellido() != null && !cliente.getApellido().isEmpty()){
                    miSentencia.setString(3, cliente.getApellido());
                }else{
                    miSentencia.setNull(3, java.sql.Types.VARCHAR);
                }
                
                miSentencia.setString(4, cliente.getContrasena());
                miSentencia.setString(5, cliente.getCorreo());
                miSentencia.setString(6, cliente.getTelefono());
                
                // campo opcional
                if(cliente.getDomicilio() != null && !cliente.getDomicilio().isEmpty()){
                    miSentencia.setString(7, cliente.getDomicilio());
                }else{
                    miSentencia.setNull(7, java.sql.Types.VARCHAR);
                }
                
                miSentencia.setString(8, cliente.getRegimenLaboral());
                
                miSentencia.setString(9, cliente.getRol());
                
                // ejecutamos
                int filasAfectadas = miSentencia.executeUpdate();
                
                if(filasAfectadas == 0){
                    throw new SQLException("Error al registrar el cliente, ninguna fila afectada");
                }
                
            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error: " + e.getMessage());
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
    public List<Cliente> read(){
        
        /*CREATE PROCEDURE listar_clientes
        AS
        BEGIN
                select
                        u.dni,
                        u.nombre,
                        u.apellido,
                        u.contrasena,
                        u.correo,
                        u.telefono,
                        c.fechaIngreso,
                        c.domicilio,
                        c.RegimenLaboral
                from cliente c
                left join usuario u on c.dni = u.dni
        END*/
        
        String sql = "{CALL listar_clientes}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql);
                ResultSet rs = miSentencia.executeQuery()){
            
            List<Cliente> lista = new ArrayList<>();
            
            while(rs.next()){
                
                Cliente cliente = Cliente.builder()
                        .dni(rs.getInt("dni"))
                        .nombre(rs.getString("nombre"))
                        .apellido(rs.getString("apellido"))
                        .contrasena(rs.getString("contrasena"))
                        .correo(rs.getString("correo"))
                        .telefono(rs.getString("telefono"))
                        .fechaIngreso(rs.getDate("fechaIngreso"))
                        .domicilio(rs.getString("domicilio"))
                        .RegimenLaboral(rs.getString("RegimenLaboral"))
                        .build();
                
                lista.add(cliente);
                
            }
            
            return lista;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }
    
    // UPDATE LISTO
    @Override
    public boolean update(Cliente cliente) {

        /*
        CREATE PROCEDURE actualizar_cliente
            @dni BIGINT,
            @contrasena VARCHAR(100),
            @telefono VARCHAR(100),
            @correo VARCHAR(100),
            @domicilio VARCHAR(100)
        AS
        BEGIN
            UPDATE usuario SET contrasena = @contrasena, telefono = @telefono, correo = @correo WHERE dni = @dni;
            UPDATE cliente SET domicilio = @domicilio WHERE dni = @dni;
        END
        */
        
        String sql = "{CALL actualizar_cliente(?, ?, ?, ?, ?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            try(CallableStatement miSentencia = miConexion.prepareCall(sql)){
                
                miSentencia.setInt(1, cliente.getDni());
                miSentencia.setString(2, cliente.getContrasena());
                miSentencia.setString(3, cliente.getTelefono());
                miSentencia.setString(4, cliente.getCorreo());
                miSentencia.setString(5, cliente.getDomicilio());

                int filasAfectadas = miSentencia.executeUpdate();

                if(filasAfectadas == 0){
                    throw new SQLException("Error en actualizar cliente, ninguna fila afectada");
                }
                
            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error en la actualizar cliente, se revertio la transaccion");
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

    // DELETE LISTO
    @Override
    public boolean delete(Cliente cliente) {

        // Para la eliminacion en cascada
        String sql = "{CALL eliminacion_completa_de_cliente(?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            try(CallableStatement miSentencia = miConexion.prepareCall(sql)){
                
                miSentencia.setInt(1, cliente.getDni());
                
                /*int filasAfectadas = miSentencia.executeUpdate();
                
                if(filasAfectadas == 0){
                    throw new SQLException("Error al elimniar cliente, ninguna fila afectada");
                }*/
                
                miSentencia.executeUpdate();
                
            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error al eliminar cliente, se revertira la operacion");
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

    // BUSCAR POR DNI LISTO
    @Override
    public Cliente searchByDni(int dni) {

        /*CREATE PROCEDURE ver_datos_del_cliente
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
                c.fechaIngreso,
                c.domicilio,
                c.RegimenLaboral AS 'regimen'
            FROM cliente c
            LEFT JOIN usuario u ON c.dni = u.dni
            WHERE c.dni = @dni
            ORDER BY u.nombre
        END*/
        
        String sql = "{CALL ver_datos_del_cliente(?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql)){
            
            miSentencia.setInt(1, dni);
            
            try(ResultSet rs = miSentencia.executeQuery()){
                
                if(rs.next()){
                    
                    Cliente cliente = Cliente.builder()
                            .dni(rs.getInt("dni"))
                            .nombre(rs.getString("nombre"))
                            .apellido(rs.getString("apellido"))
                            .contrasena(rs.getString("contrasena"))
                            .correo(rs.getString("correo"))
                            .telefono(rs.getString("telefono"))
                            .fechaIngreso(rs.getDate("fechaIngreso"))
                            .domicilio(rs.getString("domicilio"))
                            .RegimenLaboral(rs.getString("regimen"))
                            .build();
                    
                    return cliente;
                    
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