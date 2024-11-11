package com.practica.apptpi.modelo;

import java.util.Date;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter @Setter @ToString(callSuper = true)
public class Mecanico extends Usuario {
    
    private Date fechaIngreso;
    private double sueldo;

}
