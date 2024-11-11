package com.practica.apptpi.modelo;

import lombok.*;

@Getter @Setter @ToString
@Builder
public class Vehiculo {

    private int id_vehiculo;
    private String marca;
    private String modelo;
    private int anio;
    private int dni_cliente;
    
}
