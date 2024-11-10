package com.practica.apptpi.controladores;

import com.practica.apptpi.dao.ServicioDAO;
import com.practica.apptpi.modelo.Servicio;
import com.practica.apptpi.modelo.Usuario;
import java.util.*;

public class ServicioControlador {
    
    private ServicioDAO servicioDAO;
    private Scanner sc;
    
    public ServicioControlador(){
        servicioDAO = new ServicioDAO();
        sc = new Scanner(System.in);
    }
    
    // usu del metodo: create
    // solo los mecanicos tienen acceso
    public void agregarServicio(Usuario usuarioActual){
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== AGREGAR SERVICIO ==");
        
        //sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        
        System.out.print("Costo: $");
        double costo = sc.nextDouble();
        
        Servicio servicio = Servicio.builder()
                .nombre(nombre)
                .costo(costo)
                .build();
        
        if(servicioDAO.create(servicio)){
            System.out.println("Servicio agregado correctamente");
        }
        
    }
    
    // uso del metodo: read
    // tanto como cliente y mecanicos pueden acceder a este metodo
    public void listarServicios(){
        
        System.out.println("== TODOS LOS SERVICIOS ==");
        
        List<Servicio> listaServicios = servicioDAO.read();
        
        System.out.printf(
                "%-16s %-30s %-16s%n",
                "ID_SERVICIO",
                "NOMBRE",
                "COSTO");
        
        listaServicios.stream()
                .map(p -> String.format(
                        "%-16s %-30s %-16s",
                        p.getId_servicio(),
                        p.getNombre(),
                        "$" + p.getCosto()))
                .forEach(System.out::println);
        
        System.out.println("");
        
    }
    
    // usu del metodo: update
    // solo los mecanicos pueden acceder a este metodo
    public void modificarServicio(Usuario usuarioActual){
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== MODIFICAR SERVICIO ==");
        
        System.out.print("Digite el ID del Servicio: ");
        int id = sc.nextInt();
        
        Servicio servicio = servicioDAO.searchByDni(id);
        
        if(servicio == null){
            System.out.println("Error: el servicio no existe");
            return;
        }
        
        sc.nextLine();
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        
        System.out.print("Nuevo costo: $");
        double costo = sc.nextDouble();
        
        /*System.out.print("Nuevo estado (1. DISPONIBLE / 2. NO DISPONIBLE): ");
        int eleccion = sc.nextInt();
        
        // operador ternario
        String estado = (eleccion == 1) ? "DISPONIBLE" : "NO DISPONIBLE";*/
        
        servicio.setNombre(nombre);
        servicio.setCosto(costo);
       // servicio.setEstado(estado);
                
        if(servicioDAO.update(servicio)){
            System.out.println("Servicio actualizado con exito");
        }
        
    }
    
    // usu del metodo: delete
    // solo los mecanicos tienen acceso a este metodo
    public void eliminarServicio(Usuario usuarioActual){
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== ELIMINAR SERVICIO ==");
        
        System.out.print("Digite el ID del Servicio: ");
        int id = sc.nextInt();
        
        Servicio servicio = servicioDAO.searchByDni(id);
        
        if(servicio == null){
            System.out.println("Error: el servicio no existe");
            return;
        }
        
        if(servicioDAO.verificarUsoDeServicio(servicio.getId_servicio())){
            System.out.println("Error: no puedes eliminar este servicio");
            System.out.println("Porque hay clientes que lo solicitaron en sus turnos");
            return;
        }
        
        if(servicioDAO.delete(servicio)){
            System.out.println("Servicio \"" + servicio.getNombre() + "\" eliminado correctamente");
        }
        
    }
    
}
