package com.practica.apptpi.dao;

import com.practica.apptpi.conexionBD.ConectorBD;
import com.practica.apptpi.crud.OperacionesCrud;
import com.practica.apptpi.modelo.Servicio;
import java.sql.*;
import java.util.*;

public class ServicioDAO implements OperacionesCrud<Servicio>{

    @Override
    public boolean create(Servicio servicio) {
        
        String sql = "{CALL agregar_servicio(?, ?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql)){
            
            miSentencia.setString(1, servicio.getNombre());
            miSentencia.setDouble(2, servicio.getCosto());
            
            int filasAfectadas = miSentencia.executeUpdate();
            
            return filasAfectadas > 0;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public List<Servicio> read() {
        
        String sql = "SELECT * FROM servicio";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                Statement miSentencia = miConexion.createStatement();
                ResultSet rs = miSentencia.executeQuery(sql)){
            
            List<Servicio> lista = new ArrayList<>();
            
            while(rs.next()){
                
                Servicio servicio = Servicio.builder()
                        .id_servicio(rs.getInt("id_servicio"))
                        .nombre(rs.getString("nombre"))
                        .costo(rs.getDouble("costo"))
                        .estado(rs.getString("estado"))
                        .build();
                
                lista.add(servicio);
                
            }
            
            return lista;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }

    @Override
    public boolean update(Servicio servicio) {
        
        String sql = "{CALL actualizar_servicio(?, ?, ?)}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql)){
            
            miSentencia.setInt(1, servicio.getId_servicio());
            miSentencia.setString(2, servicio.getNombre());
            miSentencia.setDouble(3, servicio.getCosto());
            
            int filasAfectadas = miSentencia.executeUpdate();
            
            return filasAfectadas > 0;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }
    
    public boolean usoServicio(int id_servicio){
        
        String sqlVerUsoDeServicio = "SELECT 1 FROM turno_servicio WHERE id_servicio = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                PreparedStatement miSentencia = miConexion.prepareStatement(sqlVerUsoDeServicio)){
            
            miSentencia.setInt(1, id_servicio);
            
            try(ResultSet rs = miSentencia.executeQuery()){
                
                return rs.next();
                
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }

    @Override
    public boolean delete(Servicio servicio) {
        
        String sql = "DELETE FROM servicio WHERE id_servicio = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                PreparedStatement miSentencia = miConexion.prepareStatement(sql)){
            
            miSentencia.setInt(1, servicio.getId_servicio());
            
            int filasAfectadas = miSentencia.executeUpdate();
            
            return filasAfectadas > 0;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    @Override
    public Servicio searchByDni(int id_servicio) {
        
        String sql = "SELECT * FROM servicio WHERE id_servicio = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                PreparedStatement miSentencia = miConexion.prepareStatement(sql)){
            
            miSentencia.setInt(1, id_servicio);
            
            try(ResultSet rs = miSentencia.executeQuery()){
                
                if(rs.next()){
                    
                    Servicio servicio = Servicio.builder()
                            .id_servicio(rs.getInt("id_servicio"))
                            .nombre(rs.getString("nombre"))
                            .costo(rs.getDouble("costo"))
                            .estado(rs.getString("estado"))
                            .build();
                    
                    return servicio;
                    
                }else{
                    return null;
                }
                
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }
    
    // Metodo propio personalizado
    // Verificar si un servicio esta siendo usuado en uno o mas turnos
    public boolean verificarUsoDeServicio(int id_servicio){
        
        String sql = "SELECT 1 FROM turno_servicio WHERE id_servicio = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                PreparedStatement miSentencia = miConexion.prepareStatement(sql)){
            
            miSentencia.setInt(1, id_servicio);
            
            try(ResultSet rs = miSentencia.executeQuery()){
                
                return rs.next();
                
            }
        
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }


}
