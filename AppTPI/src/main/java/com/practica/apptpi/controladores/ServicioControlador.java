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
    public void agregarServicio(Usuario usuarioActual){
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== AGREGAR SERVICIO ==");
        
        //sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        // Capturo el posible error de entrada en costo, capturando la excepcion
        boolean entradaCorrecta = false;
        double costo = 0;
        do{
            
            try {

                System.out.print("Costo: $");
                costo = sc.nextDouble();

                entradaCorrecta = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrecta);
        
        Servicio servicio = Servicio.builder()
                .nombre(nombre)
                .costo(costo)
                .build();
        
        if(servicioDAO.create(servicio)){
            System.out.println("Servicio agregado correctamente");
        }
        
    }
    
    // uso del metodo: read
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
    public void modificarServicio(Usuario usuarioActual){
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== MODIFICAR SERVICIO ==");

        // Capturo el posible error de entrada en id, capturando la excepcion
        boolean entradaCorrectaID = false;
        int id = 0;
        do{
            
            try {

                System.out.print("Digite el ID del Servicio: ");
                id = sc.nextInt();

                entradaCorrectaID = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrectaID);
        
        Servicio servicio = servicioDAO.searchByDni(id);
        
        if(servicio == null){
            System.out.println("Error: el servicio no existe");
            return;
        }
        
        sc.nextLine();
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        
        // Capturo el posible error de entrada en costo, capturando la excepcion
        boolean entradaCorrectaCosto = false;
        double costo = 0;
        do{
            
            try {

                System.out.print("Nuevo costo: $");
                costo = sc.nextDouble();

                entradaCorrectaCosto = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrectaCosto);
        
        servicio.setNombre(nombre);
        servicio.setCosto(costo);
                
        if(servicioDAO.update(servicio)){
            System.out.println("Servicio actualizado con exito");
        }
        
    }
    
    // usu del metodo: delete
    public void eliminarServicio(Usuario usuarioActual){
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== ELIMINAR SERVICIO ==");
        
        // Capturo el posible error de entrada en id, capturando la excepcion
        boolean entradaCorrecta = false;
        int id = 0;
        do{
            
            try {

                System.out.print("Digite el ID del Servicio: ");
                id = sc.nextInt();

                entradaCorrecta = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrecta);

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
