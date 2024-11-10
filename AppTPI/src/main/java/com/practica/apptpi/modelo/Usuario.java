package com.practica.apptpi.modelo;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter @Setter
public class Usuario {

    private int dni;
    private String nombre;
    private String apellido;
    private String contrasena;
    private String correo;
    private String telefono;
    private String rol;

}
