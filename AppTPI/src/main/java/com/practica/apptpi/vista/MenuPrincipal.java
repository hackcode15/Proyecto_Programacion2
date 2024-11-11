package com.practica.apptpi.vista;

import com.practica.apptpi.autenticacion.Autenticacion;
import com.practica.apptpi.controladores.*;
import com.practica.apptpi.dao.UsuarioDAO;
import com.practica.apptpi.modelo.Usuario;
import java.util.*;

public class MenuPrincipal {

    private Scanner sc;
    private Autenticacion autenticacion;
    private UsuarioDAO usuarioDAO;
    private ClienteControlador controladorDeCliente;
    private MecanicoControlador controladorDeMecanico;
    private VehiculoControlador controladorDeVehiculo;
    private TurnoControlador controladorDeTurno;
    private ServicioControlador controladorDeServicio;
    
    public MenuPrincipal() {
        sc = new Scanner(System.in);
        autenticacion = new Autenticacion();
        usuarioDAO = new UsuarioDAO();
        controladorDeCliente = new ClienteControlador();
        controladorDeVehiculo = new VehiculoControlador();
        controladorDeTurno = new TurnoControlador();
        controladorDeMecanico = new MecanicoControlador();
        controladorDeServicio = new ServicioControlador();
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n=== SISTEMA DE TALLER AUTOMOTRIZ ===");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    menuDeLogin();
                    break;
                case 2:
                    menuRegistro();
                    break;
                case 0:
                    System.out.println("Programa finalizado");
                    return;
                default:
                    System.out.println("Opción incorrecta");
            }
        }
    }

    private void menuDeLogin() {

        System.out.println("\n=== INICIAR SESIÓN ===");

        System.out.print("DNI: ");
        int dni = sc.nextInt();

        sc.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = sc.nextLine();

        String rolDelUsuario = Autenticacion.autenticarUsuario(dni, contrasena);

        if (rolDelUsuario == null) {
            System.out.println("Error de autenticacion, dni o contraseña incorrectos");
            return;
        }

        Usuario usuario = usuarioDAO.searchByDni(dni);

        if (rolDelUsuario.equalsIgnoreCase("Cliente")) {

            menuCliente(usuario);

        } else if (rolDelUsuario.equalsIgnoreCase("Mecanico")) {

            menuMecanico(usuario);

        }

    }

    private void menuRegistro() {
        System.out.println("\n=== REGISTRARSE ===");
        System.out.println("1. Registrarse como Cliente");
        System.out.println("2. Registrarse como Mecanico");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opción: ");
        int opcion = sc.nextInt();

        switch (opcion) {

            case 1:
                controladorDeCliente.registrarseComoCliente();
                break;
            case 2:
                controladorDeMecanico.agregarMecanico();
                break;
            case 0:
                System.out.println("Volviendo al menu principal");
                break;
            default:
                System.out.println("Opcion incorrecta");
                break;

        }

    }

    private void menuMecanico(Usuario usuarioLogueado) {
        while (true) {
            // FUNCIONES DISPONIBLES PARA LOS MECANICOS TERMINADOS
            // TERMINADO
            System.out.println("\n====================== MENÚ MECANICO =========================");
            System.out.println("****************************************************************");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Actualizar mis datos");
            System.out.println("3. Eliminar mi Cuenta");
            System.out.println("****************************************************************");
            System.out.println("4. Ver lista de clientes");
            System.out.println("5. Buscar cliente por DNI");
            System.out.println("****************************************************************");
            System.out.println("6. Agregar Servicio");
            System.out.println("7. Ver lista de Servicios");
            System.out.println("8. Modificar Servicios");
            System.out.println("9. Eliminar Servicio ");
            System.out.println("****************************************************************");
            System.out.println("10. Ver todos vehiculos registrados");
            System.out.println("11. Buscar vehiculo");
            System.out.println("****************************************************************");
            System.out.println("12. Ver todos los turnos solicitados");
            System.out.println("****************************************************************");
            System.out.println("0. Cerrar Sesion");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    controladorDeMecanico.verMiInformacion(usuarioLogueado);
                    break;
                case 2:
                    controladorDeMecanico.actualizarMecanico(usuarioLogueado);
                    break;
                case 3:
                    System.out.println("== ELIMINARAS TU CUENTA DE MECANICO ==");
                    System.out.print("¿Estas seguro de eliminar tu cuenta? (Si / No):");
                    String eleccion = sc.next();

                    if (eleccion.equalsIgnoreCase("Si")) {

                        controladorDeMecanico.eliminarMecanico(usuarioLogueado);
                        
                        return;

                    } else if (eleccion.equalsIgnoreCase("No")) {
                        System.out.println("Cancelaste la eliminacion de tu cuenta");
                    } else {
                        System.out.println("Opcion incorrecta");
                    }
                    break;
                case 4:
                    controladorDeCliente.listarClientes(usuarioLogueado);
                    break;
                case 5:
                    controladorDeCliente.verDatos(usuarioLogueado);
                    break;
                case 6:
                    controladorDeServicio.agregarServicio(usuarioLogueado);
                    break;
                case 7:
                    controladorDeServicio.listarServicios();
                    break;
                case 8:
                    controladorDeServicio.modificarServicio(usuarioLogueado);
                    break;
                case 9:
                    controladorDeServicio.eliminarServicio(usuarioLogueado);
                    break;
                case 10:
                    controladorDeVehiculo.mostrarTodosLosVehiculos(usuarioLogueado);
                    break;
                case 11:
                    controladorDeVehiculo.buscarVehiculo(usuarioLogueado);
                    break;
                case 12:
                    controladorDeTurno.listarInformacionCompletaDeLosTurnos(usuarioLogueado);
                    break;
                case 0:
                    System.out.println("Cerrando sesion...");
                    return;
                default:
                    System.out.println("Opción incorrecta");
            }
        }
    }

    private void menuCliente(Usuario usuarioLogueado) {
        while (true) {
            // FUNCIONES DISPONIBLES PARA LOS CLIENTES TERMINADO
            // TERMINADO
            System.out.println("\n====================== MENÚ CLIENTE =========================");
            System.out.println("***************************************************************");
            System.out.println("1. Ver mis datos personales");
            System.out.println("2. Actualizar mis datos");
            System.out.println("3. Eliminar mi cuenta");
            System.out.println("***************************************************************");
            System.out.println("4. Agregar mi vehiculo");
            System.out.println("5. Modificar mi vehiculo");
            System.out.println("6. Retirar mi vehiculo");
            System.out.println("7. Ver mis vehiculos registrados");
            System.out.println("***************************************************************");
            System.out.println("8. Solicitar Turno para un servicio");
            System.out.println("9. Cancelar turno");
            System.out.println("10. Ver mis turnos");
            System.out.println("***************************************************************");
            System.out.println("0. Cerrar sesion");
            System.out.print("Seleccione una opción: ");
            int opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    controladorDeCliente.verDatos(usuarioLogueado);
                    break;
                case 2:
                    controladorDeCliente.modificarMisDatos(usuarioLogueado);
                    break;
                case 3:
                    System.out.println("AL ELIMINAR TU CUENTA EN CASO DE TENER PEDIDOS DE TURNOS PARA SERVICIOS Y TUS VEHICULOS REGISTRADOS TAMBIEN SERAN ELIMINADOS");
                    System.out.print("¿Estas seguro de eliminar tu cuenta? (Si / No):");
                    String eleccion = sc.next();

                    if (eleccion.equalsIgnoreCase("Si")) {

                        controladorDeCliente.eliminarMiCuenta(usuarioLogueado);
                        
                        return; // me lleva a menu inicial

                    } else if (eleccion.equalsIgnoreCase("No")) {
                        System.out.println("Cancelaste la eliminacion de tu cuenta");
                    } else {
                        System.out.println("Opcion incorrecta");
                    }
                    break;
                case 4:
                    controladorDeVehiculo.agregarVehiculo(usuarioLogueado);
                    break;
                case 5:
                    controladorDeVehiculo.actualizarVehiculo(usuarioLogueado);
                    break;
                case 6:
                    controladorDeVehiculo.eliminarVehiculo(usuarioLogueado);
                    break;
                case 7:
                    controladorDeVehiculo.verMisVehiculosRegistrados(usuarioLogueado);
                    break;
                case 8:
                    controladorDeTurno.solicitarTurno(usuarioLogueado);
                    break;
                case 9:
                    controladorDeTurno.cancelarTurno(usuarioLogueado);
                    break;
                case 10:
                    controladorDeTurno.listarMisTurnos(usuarioLogueado);
                    break;
                case 0:
                    System.out.println("Cerrando sesion...");
                    return;
                default:
                    System.out.println("Opción incorrecta");
                    break;
            }
        }
    }
    
}
