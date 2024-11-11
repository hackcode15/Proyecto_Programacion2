package com.practica.apptpi.modelo;

import java.util.Date;
import lombok.*;

@Getter @Setter @ToString
@Builder
public class Turno {
    
    private int id_turno;
    private Date fecha;
    private int id_vehiculo;
    private int dni_cliente;
    
}