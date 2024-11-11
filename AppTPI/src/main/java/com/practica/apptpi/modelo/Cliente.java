package com.practica.apptpi.modelo;

import java.util.Date;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter @Setter @ToString(callSuper = true)
public class Cliente extends Usuario{
    
    private Date fechaIngreso;
    private String domicilio;
    private String RegimenLaboral;

    // Cliente
    /*
    solo puede hacer
    
    registrarse
    eliminar su cuenta
    modificar sus datos
    ver todos sus datos
    */
    
    // Mecanico
    /*
    solo puede hacer
    
    eliminar cliente
    ver todos los clientes
    */

}
