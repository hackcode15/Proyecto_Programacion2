package com.practica.apptpi.dao;

import com.practica.apptpi.conexionBD.ConectorBD;
import com.practica.apptpi.crud.OperacionesCrud;
import com.practica.apptpi.dto.VehiculoDTO;
import com.practica.apptpi.modelo.Vehiculo;
import java.sql.*;
import java.util.*;

public class VehiculoDAO implements OperacionesCrud<Vehiculo> {

    @Override
    public boolean create(Vehiculo vehiculo) {

        String sql = "{CALL agregar_vehiculo(?, ?, ?, ?)}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql)) {

            miSentencia.setString(1, vehiculo.getMarca());
            miSentencia.setString(2, vehiculo.getModelo());
            miSentencia.setInt(3, vehiculo.getAnio());
            miSentencia.setInt(4, vehiculo.getDni_cliente());

            int filasAfectadas = miSentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    @Override
    public List<Vehiculo> read() {

        String sql = "SELECT * FROM vehiculo";

        try (Connection miConexion = ConectorBD.dameConexion(); Statement miSentencia = miConexion.createStatement(); ResultSet rs = miSentencia.executeQuery(sql)) {

            List<Vehiculo> lista = new ArrayList<>();

            while (rs.next()) {

                Vehiculo vehiculo = Vehiculo.builder()
                        .id_vehiculo(rs.getInt("id_vehiculo"))
                        .marca(rs.getString("marca"))
                        .modelo(rs.getString("modelo"))
                        .anio(rs.getInt("anio"))
                        .build();

                lista.add(vehiculo);

            }

            return lista;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    @Override
    public boolean update(Vehiculo vehiculo) {

        String sql = "{CALL actualizar_vehiculo(?, ?, ?, ?, ?)}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql)) {

            miSentencia.setInt(1, vehiculo.getId_vehiculo());
            miSentencia.setString(2, vehiculo.getMarca());
            miSentencia.setString(3, vehiculo.getModelo());
            miSentencia.setInt(4, vehiculo.getAnio());
            miSentencia.setInt(5, vehiculo.getDni_cliente());

            int filasAfectadas = miSentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean delete(Vehiculo vehiculo) {

        /*String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";

        try (Connection miConexion = ConectorBD.dameConexion(); PreparedStatement miSentencia = miConexion.prepareStatement(sql)) {

            miSentencia.setInt(1, vehiculo.getId_vehiculo());

            int filasAfectadas = miSentencia.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }*/
        
        String sqlBuscarTurnoAsociados = "SELECT id_turno FROM turno WHERE id_vehiculo = ?";
        String sqlEliminarTurnoServicio = "DELETE FROM turno_servicio WHERE id_turno = ?";
        String sqlEliminarTurnos = "DELETE FROM turno WHERE id_vehiculo = ?";
        String sqlEliminarVehiculo = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            List<Integer> idDeLosTurnos = new ArrayList<>();
            
            try{
               
                try(PreparedStatement miSentenciaBuscarID = miConexion.prepareStatement(sqlBuscarTurnoAsociados)){
                    
                    miSentenciaBuscarID.setInt(1, vehiculo.getId_vehiculo());
                
                    try(ResultSet rs = miSentenciaBuscarID.executeQuery()){ // 1 ejecucion
                        
                        while(rs.next()){
                            idDeLosTurnos.add(rs.getInt("id_turno"));
                        }
                        
                    }
                
                }
                
                if(!idDeLosTurnos.isEmpty()){ // Si tiene turno asociados
                    
                    try(PreparedStatement miSentenciaEliminarTurnoServicio = miConexion.prepareStatement(sqlEliminarTurnoServicio)){
                        
                        for (Integer idTurno : idDeLosTurnos) {
                            
                            miSentenciaEliminarTurnoServicio.setInt(1, idTurno);
                            
                            miSentenciaEliminarTurnoServicio.executeUpdate(); // 2 ejecucion
                            
                        }
                        
                    }
                    
                    try(PreparedStatement miSentenciaEliminarTurno = miConexion.prepareStatement(sqlEliminarTurnos)){
                        
                        miSentenciaEliminarTurno.setInt(1, vehiculo.getId_vehiculo());
                        
                        miSentenciaEliminarTurno.executeUpdate();
                        
                    }
                    
                }
                
                try(PreparedStatement miSentenciaEliminarVehiculo = miConexion.prepareStatement(sqlEliminarVehiculo)){
                    
                    miSentenciaEliminarVehiculo.setInt(1, vehiculo.getId_vehiculo());
                    
                    miSentenciaEliminarVehiculo.executeUpdate();
                    
                }
                

            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error al eliminar vehiculo, se revertio la operacion");
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
    public Vehiculo searchByDni(int id_vehiculo) {

        String sql = "{CALL buscar_vehiculo(?)}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql)) {

            miSentencia.setInt(1, id_vehiculo);

            try (ResultSet rs = miSentencia.executeQuery()) {

                if (rs.next()) {

                    Vehiculo vehiculo = Vehiculo.builder()
                            .id_vehiculo(rs.getInt("id_vehiculo"))
                            .marca(rs.getString("marca"))
                            .modelo(rs.getString("modelo"))
                            .anio(rs.getInt("anio"))
                            .dni_cliente(rs.getInt("dni_cliente"))
                            .build();

                    return vehiculo;

                } else {
                    return null;
                }

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    // Metodo propio especifico
    // Verificar que el vehiculo para actualizar sea de ese cliente
    public boolean verificarTuVehiculo(int id_vehiculo, int dni_cliente) {

        String sql = "{CALL verificar_vehiculo_a_modificar(?, ?)}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql)) {

            miSentencia.setInt(1, id_vehiculo);
            miSentencia.setInt(2, dni_cliente);

            try (ResultSet rs = miSentencia.executeQuery()) {

                return rs.next();

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }

    }

    // Metodo propio especifico
    public List<Vehiculo> listarTusVehiculosRegistrados(int dni_cliente) {

        String sql = "{CALL listar_vehiculos_de_un_cliente(?)}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql)) {

            miSentencia.setInt(1, dni_cliente);

            List<Vehiculo> lista = new ArrayList<>();

            try (ResultSet rs = miSentencia.executeQuery()) {

                while (rs.next()) {

                    Vehiculo vehiculo = Vehiculo.builder()
                            .id_vehiculo(rs.getInt("id_vehiculo"))
                            .marca(rs.getString("marca"))
                            .modelo(rs.getString("modelo"))
                            .anio(rs.getInt("anio"))
                            .build();

                    lista.add(vehiculo);

                }

            }

            return lista;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }
    
    // metodo propio personalizado
    public List<VehiculoDTO> listarTodosLosVehiculosRegistrados(){
//        
        /*CREATE PROCEDURE listar_info_completa_de_vehiculo
        AS
        BEGIN
                SELECT 
                        v.id_vehiculo AS 'id', 
                        u.nombre AS 'cliente', 
                        v.marca, 
                        v.modelo, 
                        v.anio AS 'año'
                FROM vehiculo v
                LEFT JOIN cliente c ON v.dni_cliente = c.dni
                LEFT JOIN usuario u ON c.dni = u.dni
                ORDER BY u.nombre;
        END*/
        
        String sql = "{CALL listar_info_completa_de_vehiculo}";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                CallableStatement miSentencia = miConexion.prepareCall(sql);
                ResultSet rs = miSentencia.executeQuery()){
            
            List<VehiculoDTO> lista = new ArrayList<>();
            
            while(rs.next()){
                
                VehiculoDTO vehiculo = VehiculoDTO.builder()
                        .id_vehiculo(rs.getInt("id"))
                        .nombre(rs.getString("cliente"))
                        .marca(rs.getString("marca"))
                        .modelo(rs.getString("modelo"))
                        .anio(rs.getInt("año"))
                        .build();
                
                lista.add(vehiculo);
                
            }
            
            return lista;
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        
    }

}
