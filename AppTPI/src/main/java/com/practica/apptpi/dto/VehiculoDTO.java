package com.practica.apptpi.dto;

import lombok.*;

@Getter @Setter @ToString
@Builder
public class VehiculoDTO {

    // de la clase vehiculo
    private int id_vehiculo;
    private String marca;
    private String modelo;
    private int anio;
    
    // de usuario
    private String nombre;
    
}
