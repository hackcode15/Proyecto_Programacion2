package com.practica.apptpi.controladores;

import com.practica.apptpi.dao.ServicioDAO;
import com.practica.apptpi.dao.TurnoDAO;
import com.practica.apptpi.dto.TurnoDTO;
import com.practica.apptpi.dao.VehiculoDAO;
import com.practica.apptpi.modelo.Servicio;
import com.practica.apptpi.modelo.Turno;
import com.practica.apptpi.modelo.Usuario;
import com.practica.apptpi.modelo.Vehiculo;
import java.util.*;

public class TurnoControlador {

    private TurnoDAO turnoDAO;
    private VehiculoDAO vehiculoDAO;
    private ServicioDAO servicioDAO;
    private Scanner sc;

    public TurnoControlador() {
        turnoDAO = new TurnoDAO();
        vehiculoDAO = new VehiculoDAO();
        servicioDAO = new ServicioDAO();
        sc = new Scanner(System.in);
    }

    // uso del metodo: create
    // solo los clientes pueden cargar turnos
    public void solicitarTurno(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }

        System.out.println("== SOLICITA UN TURNO DE SERVICIO PARA TU VEHICULO ==");

        System.out.print("Agregue su vehiculo con su ID: ");
        int id_vehiculo = sc.nextInt();

        Vehiculo vehiculo = vehiculoDAO.searchByDni(id_vehiculo);

        if (vehiculo == null) {
            System.out.println("Error: el vehiculo no existe");
            return;
        }

        if (!vehiculoDAO.verificarTuVehiculo(id_vehiculo, usuarioActual.getDni())) {
            System.out.println("Error: no puedes añadir un vehiculo que no te pertenece");
            return;
        }

        List<Servicio> serviciosSeleccionados = new ArrayList<>();
        boolean seguirAgregandoServicio = true;

        do {

            System.out.println("== SELECCIONE EL SERVICIO ==");
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

            System.out.print("Digite el ID del servicio a solicitar: ");
            int id_servicio = sc.nextInt();

            Servicio servicio = servicioDAO.searchByDni(id_servicio);

            if (servicio == null) {
                System.out.println("Error: el servicio no existe");
            } else {
                serviciosSeleccionados.add(servicio);
                System.out.println("Servicio agregado: " + servicio.getNombre());
            }

            System.out.print("¿Quiere solicitar otro servicio? (Si / No)");
            String eleccion = sc.next();

            if (eleccion.equalsIgnoreCase("si")) {
                seguirAgregandoServicio = true;
            } else {
                seguirAgregandoServicio = false;
            }

        } while (seguirAgregandoServicio);

        if (!serviciosSeleccionados.isEmpty()) {

            Turno turno = Turno.builder()
                    .id_vehiculo(id_vehiculo)
                    .dni_cliente(usuarioActual.getDni())
                    .build();

            if (turnoDAO.create(turno, serviciosSeleccionados)) {
                System.out.println("Turno creado correctamente");
                System.out.println("Vehiculo cargado: " + vehiculo.getMarca() + ", " + vehiculo.getModelo());
                System.out.println("Servicios solicitados");
                serviciosSeleccionados.stream()
                        .map(p -> "Servicio: " + p.getNombre())
                        .forEach(System.out::println);
            }

        }else{
            System.out.println("Error: no se seleccionaron servicios");
        }

    }

    // uso del metodo personalizado: leerTurnos
    // solo los mecanicos pueden acceder
    public void listarInformacionCompletaDeLosTurnos(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Mecanico")) {
            System.out.println("Acceso denegado");
            return;
        }

        List<TurnoDTO> listaTurnos = turnoDAO.leerTurnos();

        System.out.println("== TODOS LOS TURNOS REGISTRADOS ==");

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-16s %-16s %-30s %-16s%n",
                "ID_TURNO",
                "FECHA",
                "MARCA",
                "MODELO",
                "NOMBRE",
                "APELLIDO",
                "SERVICIO",
                "COSTO");

        listaTurnos.stream()
                .map(p -> String.format(
                "%-16s %-16s %-16s %-16s %-16s %-16s %-30s %-16s",
                p.getId_turno(),
                p.getFecha_turno(),
                p.getMarca_vehiculo(),
                p.getModelo_vehiculo(),
                p.getNombre_usuario(),
                p.getApellido_usuario(),
                p.getNombre_servicio(),
                p.getCosto_servicio()))
                .forEach(System.out::println);

        System.out.println("");

    }

    // uso del metodo: leerTurnosDeUnCliente
    // solo los clientes tienen acceso
    public void listarMisTurnos(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }

        List<TurnoDTO> listaTurnos = turnoDAO.leerTurnosDeUnCliente(usuarioActual.getDni());

        System.out.println("== TODOS TUS TURNOS SOLICITADOS ==");

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-30s %-16s%n",
                "ID_TURNO",
                "FECHA",
                "MARCA",
                "MODELO",
                "SERVICIO",
                "COSTO");

        listaTurnos.stream()
                .map(p -> String.format(
                "%-16s %-16s %-16s %-16s %-30s %-16s",
                p.getId_turno(),
                p.getFecha_turno(),
                p.getMarca_vehiculo(),
                p.getModelo_vehiculo(),
                p.getNombre_servicio(),
                p.getCosto_servicio()))
                .forEach(System.out::println);

        System.out.println("");

    }
    
    // uso del metodo: delete
    public void cancelarTurno(Usuario usuarioActual){
        
        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Acceso denegado");
            return;
        }
        
        System.out.println("== CANCELAR TURNOS SOLICITADOS ==");
        
        listarMisTurnos(usuarioActual);
        
        System.out.println("Digite el ID del turno a eliminar: ");
        int id_turno = sc.nextInt();
        
        Turno turno = turnoDAO.searchByDni(id_turno);
        
        if(turno == null){
            System.out.println("Error: el turno no existe");
            return;
        }
        
        if(!turnoDAO.verificarTurno(id_turno, usuarioActual.getDni())){
            System.out.println("Error: no puedes cancelar turnos que no te pertenece");
            return;
        }
        
        if(turnoDAO.delete(turno)){
            System.out.println("Turno cancelado correctamente");
        }
        
    }

    
    
}
