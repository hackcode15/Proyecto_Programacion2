package com.practica.apptpi.controladores;

import com.practica.apptpi.dao.VehiculoDAO;
import com.practica.apptpi.dto.VehiculoDTO;
import com.practica.apptpi.modelo.Usuario;
import com.practica.apptpi.modelo.Vehiculo;
import java.util.*;

public class VehiculoControlador {

    private VehiculoDAO vehiculoDAO;
    private Scanner sc;

    public VehiculoControlador() {
        vehiculoDAO = new VehiculoDAO();
        sc = new Scanner(System.in);
    }

    // usu del metodo: create
    // Acesso solo para los clientes
    public void agregarVehiculo(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }

        System.out.println("== AGREGAR VEHICULO ==");

        System.out.print("Marca: ");
        String marca = sc.nextLine();

        System.out.print("Modelo: ");
        String modelo = sc.nextLine();

        System.out.print("Año: ");
        int anio = sc.nextInt();

        Vehiculo vehiculo = Vehiculo.builder()
                .marca(marca)
                .modelo(modelo)
                .anio(anio)
                .dni_cliente(usuarioActual.getDni())
                .build();

        if (vehiculoDAO.create(vehiculo)) {
            System.out.println("Vehiculo agreagado correctamente");
        }

    }

    // uso del metodo: read
    // Solo los mecanicos tienen acceso a ver la lista de todos los vehiculos
    public void listarVehiculos(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Mecanico")) {
            System.out.println("Acceso denegado");
            return;
        }

        System.out.println("== LISTA DE VEHICULOS ==");

        List<Vehiculo> listaVehiculos = vehiculoDAO.read();

        System.out.printf(
                "%-16s %-16s %-16s %-16s%n",
                "ID_VEHICULO",
                "MARCA",
                "MODELO",
                "AÑO");

        listaVehiculos.stream()
                .map(p -> String.format("%-16s %-16s %-16s %-16s",
                p.getId_vehiculo(),
                p.getMarca(),
                p.getModelo(),
                p.getAnio()))
                .forEach(System.out::println);

        System.out.println("");

    }

    // usu del metodo: update
    // solo los clientes pueden modificar sus vehiculos
    public void actualizarVehiculo(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }

        System.out.println("== ACTUALIZAR LOS DATOS DE TU VEHICULO ==");

        System.out.print("Digite el ID de su vehiculo: ");
        int id = sc.nextInt();

        Vehiculo vehiculo = vehiculoDAO.searchByDni(id);

        if (vehiculo == null) {
            System.out.println("Error: el vehiculo no existe");
        }

        if (!vehiculoDAO.verificarTuVehiculo(id, usuarioActual.getDni())) {
            System.out.println("No puedes modificar los datos de un vehiculo que no te pertenece");
            return;
        }

        sc.nextLine();
        System.out.print("Nueva marca: ");
        String marca = sc.nextLine();

        System.out.print("Nuevo modelo: ");
        String modelo = sc.nextLine();

        System.out.print("Nuevo año: ");
        int anio = sc.nextInt();

        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio(anio);

        if (vehiculoDAO.update(vehiculo)) {
            System.out.println("Has actualizado tu vehiculo correctamente");
        }

    }

    // usu del metodo: delete
    // solo los clientes pueden eliminar su vehiculo
    public void eliminarVehiculo(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }

        System.out.println("== RETIRAR TU VEHICULO ==");
        System.out.println("SI EL VEHICULO A RETIRAR TIENE TURNOS SOLICITADOS SE ELIMINARAN");
        System.out.println("¿Estas seguro que quieres eliminar el vehiculo? (SI / NO): ");
        String eleccion = sc.next();

        if (eleccion.equalsIgnoreCase("si")) {

            System.out.print("Digite el ID de su vehiculo: ");
            int id = sc.nextInt();

            Vehiculo vehiculo = vehiculoDAO.searchByDni(id);

            if (vehiculo == null) {
                System.out.println("Error: el vehiculo no existe");
                return;
            }

            if (!vehiculoDAO.verificarTuVehiculo(id, usuarioActual.getDni())) {
                System.out.println("No puedes retirado un vehiculo que no te pertenece");
                return;
            }

            if (vehiculoDAO.delete(vehiculo)) {
                System.out.println("Cliente \"" + usuarioActual.getNombre() + "\" has retirado correctamente tu vehiculo");
            }

        }else if(eleccion.equalsIgnoreCase("no")){
            System.out.println("Cancelaste la eliminacion de tu vehiculo");
        }else{
            System.out.println("Opcion incorrecta");
        }

    }

    // usu del metodo: searchByDni - search by id_vehiculo
    // solo los mecanicos pueden tener acceso
    public void buscarVehiculo(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Mecanico")) {
            System.out.println("Acceso denegado");
            return;
        }

        System.out.println("== BUSCA UN VEHICULO == ");

        System.out.print("Digite el ID del vehiculo: ");
        int id = sc.nextInt();

        Vehiculo vehiculo = vehiculoDAO.searchByDni(id);

        if (vehiculo == null) {
            System.out.println("Error: el vehiculo no existe");
            return;
        }

        System.out.println("Vehiculo encontrado");

        System.out.printf(
                "%-16s %-16s %-16s %-16s%n",
                "ID_VEHICULO",
                "MARCA",
                "MODELO",
                "AÑO");

        System.out.printf(
                "%-16s %-16s %-16s %-16s",
                vehiculo.getId_vehiculo(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio());

        System.out.println("");

    }

    // usu del metodo: listarTusVehiculosRegistrados
    // Solo los clientes tienen acceso
    public void verMisVehiculosRegistrados(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }

        List<Vehiculo> misVehiculos = vehiculoDAO.listarTusVehiculosRegistrados(usuarioActual.getDni());

        System.out.printf(
                "%-16s %-16s %-16s %-16s%n",
                "ID_VEHICULO",
                "MARCA",
                "MODELO",
                "AÑO");

        misVehiculos.stream()
                .map(p -> String.format("%-16s %-16s %-16s %-16s",
                p.getId_vehiculo(),
                p.getMarca(),
                p.getModelo(),
                p.getAnio()))
                .forEach(System.out::println);

        System.out.println("");

    }
    
    // uso del metodo: listarTodosLosVehiculosRegistrados
    // solo los mecanicos tienen acceso
    public void mostrarTodosLosVehiculos(Usuario usuarioActual){
        
        System.out.println("== TODOS LOS VEHICULOS REGISTRADOS ==");
        
        List<VehiculoDTO> listaVehiculos = vehiculoDAO.listarTodosLosVehiculosRegistrados();
        
        if(listaVehiculos.isEmpty()){
            System.out.println("Error: no hay vehiculos registrados");
            return;
        }
        
        System.out.printf(
                "%-16s %-16s %-16s %-16s %-16s%n",
                "ID",
                "CLIENTE",
                "MARCA",
                "MODELO",
                "AÑO");
        
        listaVehiculos.stream()
                .map(p -> String.format(
                        "%-16s %-16s %-16s %-16s %-16s", 
                        p.getId_vehiculo(),
                        p.getNombre(),
                        p.getMarca(),
                        p.getModelo(),
                        p.getAnio()))
                .forEach(System.out::println);
        
        System.out.println("");
        
    }

}
