package com.practica.apptpi.modelo;

import lombok.*;

@Getter @Setter @ToString
@Builder
public class Servicio {

    private int id_servicio;
    private String nombre;
    private double costo;
    private String estado;

}
