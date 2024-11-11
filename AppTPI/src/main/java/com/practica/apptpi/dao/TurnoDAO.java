package com.practica.apptpi.dao;

import com.practica.apptpi.dto.TurnoDTO;
import com.practica.apptpi.conexionBD.ConectorBD;
import com.practica.apptpi.crud.OperacionesCrudAdaptadora;
import com.practica.apptpi.modelo.Servicio;
import com.practica.apptpi.modelo.Turno;
import java.sql.*;
import java.util.Date;
import java.util.*;

public class TurnoDAO extends OperacionesCrudAdaptadora<Turno> {

    // este create personalizado es para agregar turno y servicio juntos
    public boolean create(Turno turno, List<Servicio> servicios) {

        String sql_turno = "INSERT INTO turno(id_vehiculo, dni_cliente) VALUES(?, ?)";
        String sql_turno_servicio = "INSERT INTO turno_servicio(id_turno, id_servicio) VALUES(?, ?)";

        try (Connection miConexion = ConectorBD.dameConexion()) {

            miConexion.setAutoCommit(false);

            try (PreparedStatement miSentenciaTurno = miConexion.prepareStatement(sql_turno, Statement.RETURN_GENERATED_KEYS)) {

                miSentenciaTurno.setInt(1, turno.getId_vehiculo());
                miSentenciaTurno.setInt(2, turno.getDni_cliente());

                miSentenciaTurno.executeUpdate();

                try (ResultSet rs = miSentenciaTurno.getGeneratedKeys()) {

                    if (rs.next()) {

                        int idTurnoGenerado = rs.getInt(1);

                        try (PreparedStatement miSentenciaTurnoServicio = miConexion.prepareStatement(sql_turno_servicio)) {

                            for (Servicio servicio : servicios) {

                                miSentenciaTurnoServicio.setInt(1, idTurnoGenerado);
                                miSentenciaTurnoServicio.setInt(2, servicio.getId_servicio());

                                miSentenciaTurnoServicio.executeUpdate();

                            }

                        }

                    }

                }

            } catch (SQLException e) {
                miConexion.rollback();
                System.out.println("Error al registrar el turno, se revertio la operacion");
                return false;
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
    public boolean delete(Turno turno) {
        
        String sqlEliminarTurnoServicio = "DELETE FROM turno_servicio WHERE id_turno = ?";
        String sqlEliminarTurnos = "DELETE FROM turno WHERE id_turno = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion()){
            
            miConexion.setAutoCommit(false);
            
            try{
                
                try(PreparedStatement miSentenciaEliminarTurnoServicio = miConexion.prepareStatement(sqlEliminarTurnoServicio)){
                    
                    miSentenciaEliminarTurnoServicio.setInt(1, turno.getId_turno());
                    
                    miSentenciaEliminarTurnoServicio.executeUpdate();
                    
                }
                
                try(PreparedStatement miSentenciaEliminarTurno = miConexion.prepareStatement(sqlEliminarTurnos)){
                    
                    miSentenciaEliminarTurno.setInt(1, turno.getId_turno());
                    
                    miSentenciaEliminarTurno.executeUpdate();
                    
                }
                
                
            }catch(SQLException e){
                miConexion.rollback();
                System.out.println("Error al eliminar el turno, se revertio la operacion");
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
    public Turno searchByDni(int id_turno) {

        String sql = "SELECT * FROM turno WHERE id_turno = ?";

        try (Connection miConexion = ConectorBD.dameConexion(); PreparedStatement miSentencia = miConexion.prepareStatement(sql)) {

            miSentencia.setInt(1, id_turno);

            try (ResultSet rs = miSentencia.executeQuery()) {

                if (rs.next()) {

                    Turno turno = Turno.builder()
                            .id_turno(rs.getInt("id_turno"))
                            .dni_cliente(rs.getInt("dni_cliente"))
                            .id_vehiculo(rs.getInt("id_vehiculo"))
                            .build();

                    return turno;

                } else {
                    return null;
                }

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    // metodo propio especifico
    // listar la informacion completa de los turnos
    public List<TurnoDTO> leerTurnos() {

        String sql = "{CALL listar_info_completa_de_turno}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql); ResultSet rs = miSentencia.executeQuery()) {

            List<TurnoDTO> lista = new ArrayList<>();

            while (rs.next()) {

                TurnoDTO turno = TurnoDTO.builder()
                        .id_turno(rs.getInt("id_turno"))
                        .fecha_turno(rs.getDate("fecha"))
                        .marca_vehiculo(rs.getString("marca"))
                        .modelo_vehiculo(rs.getString("modelo"))
                        .nombre_usuario(rs.getString("nombre"))
                        .apellido_usuario(rs.getString("apellido"))
                        .nombre_servicio(rs.getString("servicio"))
                        .costo_servicio(rs.getDouble("costo"))
                        .build();

                lista.add(turno);

            }

            return lista;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    
    public List<TurnoDTO> leerTurnosDeUnCliente(int dni) {

        /*CREATE PROCEDURE ver_mis_turnos
                @dni BIGINT
        AS
        BEGIN
                SELECT 
                        t.id_turno, 
                        t.fecha, 
                        v.marca, 
                        v.modelo, 
                        s.nombre AS servicio, 
                        s.costo
                FROM turno t
                JOIN turno_servicio ts ON t.id_turno = ts.id_turno
                JOIN servicio s ON ts.id_servicio = s.id_servicio
                JOIN vehiculo v ON t.id_vehiculo = v.id_vehiculo
                JOIN cliente c ON t.dni_cliente = c.dni
                JOIN usuario u ON c.dni = u.dni
                WHERE c.dni = @dni;
        END*/
        String sql = "{CALL ver_mis_turnos(?)}";

        try (Connection miConexion = ConectorBD.dameConexion(); CallableStatement miSentencia = miConexion.prepareCall(sql)) {

            miSentencia.setInt(1, dni);

            List<TurnoDTO> lista = new ArrayList<>();

            try (ResultSet rs = miSentencia.executeQuery()) {

                while (rs.next()) {

                    TurnoDTO turno = TurnoDTO.builder()
                            .id_turno(rs.getInt("id_turno"))
                            .fecha_turno(rs.getDate("fecha"))
                            .marca_vehiculo(rs.getString("marca"))
                            .modelo_vehiculo(rs.getString("modelo"))
                            .nombre_servicio(rs.getString("servicio"))
                            .costo_servicio(rs.getDouble("costo"))
                            .build();

                    lista.add(turno);

                }

            }

            return lista;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }
    
    public boolean verificarTurno(int id_turno, int dni_cliente){
        
        String sql = "SELECT 1 FROM turno WHERE id_turno = ? and dni_cliente = ?";
        
        try(Connection miConexion = ConectorBD.dameConexion();
                PreparedStatement miSentencia = miConexion.prepareStatement(sql)){
            
            miSentencia.setInt(1, id_turno);
            miSentencia.setInt(2, dni_cliente);
            
            try(ResultSet rs = miSentencia.executeQuery()){
                
                return rs.next();
                
            }
            
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        
    }

}
