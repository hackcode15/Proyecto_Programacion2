package com.practica.apptpi.dto;

import java.util.Date;
import lombok.*;

@Getter @Setter @ToString
@Builder
// Creacion de una clase DTO (Data Transfer Object)
public class TurnoDTO {
    
    // Definimos los datos que queremos mostrar en la consulta
    // de turno
    private int id_turno;
    private Date fecha_turno;
    
    // de vehiculo
    private String marca_vehiculo;
    private String modelo_vehiculo;
    
    // de usuario
    private String nombre_usuario;
    private String apellido_usuario;
    
    // de servicio
    private String nombre_servicio;
    private double costo_servicio;
    
    
}
