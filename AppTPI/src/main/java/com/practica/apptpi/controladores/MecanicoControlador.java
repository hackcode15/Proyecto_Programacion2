package com.practica.apptpi.controladores;

import com.practica.apptpi.dao.MecanicoDAO;
import com.practica.apptpi.modelo.Mecanico;
import com.practica.apptpi.modelo.Usuario;
import java.util.*;

public class MecanicoControlador {

    private MecanicoDAO mecanicoDAO;
    private Scanner sc;

    public MecanicoControlador() {
        this.mecanicoDAO = new MecanicoDAO();
        this.sc = new Scanner(System.in);
    }

    // usu del metodo: create
    public void agregarMecanico() {

        System.out.println("== AGREGAR MECANICO ==");

        // Capturo el posible error de entrada en dni, capturando la excepcion
        boolean entradaCorrecta = false;
        int dni = 0;
        do{
            
            try {

                System.out.print("Digite su DNI: ");
                dni = sc.nextInt();

                entradaCorrecta = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrecta);

        sc.nextLine();
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        // Opcional
        System.out.print("Apellido (enter para omitir): ");
        String apellido = sc.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = sc.nextLine();

        System.out.print("Correo: ");
        String correo = sc.nextLine();

        System.out.print("Telefono: ");
        String telefono = sc.nextLine();

        // Capturo el posible error de entrada en sueldo, capturando la excepcion
        boolean entradaCorrectaID = false;
        double sueldo = 0;
        do{
            
            try {

                System.out.print("Sueldo $: ");
                sueldo = sc.nextDouble();

                entradaCorrectaID = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrectaID);
        
        Mecanico mecanico = Mecanico.builder()
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .contrasena(contrasena)
                .correo(correo)
                .telefono(telefono)
                .sueldo(sueldo)
                .rol("Mecanico")
                .build();

        if (mecanicoDAO.create(mecanico)) {
            System.out.println("\"" + mecanico.getNombre() + "\" has creado tu cuenta de mecanico correctamente");
            System.out.println("Ahora inicia sesion con tu nueva cuenta!!");
        }

    }

    // usu del metodo: read
    public void listarMecanicos() {

        System.out.println("== LISTA DE MECANICOS ==");

        List<Mecanico> listaMecanicos = mecanicoDAO.read();

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-16s %-16s%n",
                "DNI",
                "NOMBRE",
                "APELLIDO",
                "TELEFONO",
                "FECHA_INGRESO",
                "SUELDO");

        listaMecanicos.stream()
                .map(p -> String.format(
                "%-16s %-16s %-16s %-16s %-16s %-16s",
                p.getDni(),
                p.getNombre(),
                p.getApellido(),
                p.getTelefono(),
                p.getFechaIngreso(),
                "$" + p.getSueldo()))
                .forEach(System.out::println);

        System.out.println("");

    }

    // uso del metodo: update
    public void actualizarMecanico(Usuario usuarioActual) {

        System.out.println("== ACTUALIZAR TUS DATOS ==");

        Mecanico mecanico = mecanicoDAO.searchByDni(usuarioActual.getDni());

        if (mecanico == null) {
            System.out.println("Error: el mecanico no existe");
            return;
        }

        System.out.print("Nueva contraseña: ");
        String nuevaContrasena = sc.nextLine();

        System.out.print("Nuevo Telefono: ");
        String nuevaTelefono = sc.nextLine();

        System.out.print("Nuevo Correo: ");
        String nuevoCorreo = sc.nextLine();
    
        // Capturo el posible error de entrada en sueldo, capturando la excepcion
        boolean entradaCorrectaID = false;
        double nuevoSueldo = 0;
        do{
            
            try {

                System.out.print("Nuevo sueldo $: ");
                nuevoSueldo = sc.nextDouble();

                entradaCorrectaID = true;

            } catch (InputMismatchException e) {
                System.out.println("Error: debes introducir un valor numerico");
                sc.nextLine();
            }

        }while(!entradaCorrectaID);

        mecanico.setContrasena(nuevaContrasena);
        mecanico.setTelefono(nuevaTelefono);
        mecanico.setCorreo(nuevoCorreo);
        mecanico.setSueldo(nuevoSueldo);

        if (mecanicoDAO.update(mecanico)) {
            System.out.println("Mecanico \"" + mecanico.getNombre() + "\" has actualizado tus datos correctamente");
        }

    }

    // uso del metodo: delete
    public void eliminarMecanico(Usuario usuarioActual) {
        
        if(!usuarioActual.getRol().equalsIgnoreCase("Mecanico")){
            System.out.println("Error acceso denegado");
            return;
        }
        
        Mecanico mecanico = mecanicoDAO.searchByDni(usuarioActual.getDni());

        if (mecanico != null) {

            if (mecanicoDAO.delete(mecanico)) {
                System.out.println("Mecanico \"" + mecanico.getNombre() + "\" has eliminado tu cuenta correctamente");
            }

        } else {
            System.out.println("Error el mecanico no existe");
        }

    }

    // usu del metodo: searchByDni
    public void verMiInformacion(Usuario usuarioActual) {

        System.out.println("== INFORMACION PERSONAL DE TU CUENTA ==");

        Mecanico mecanico = mecanicoDAO.searchByDni(usuarioActual.getDni());

        if (mecanico == null) {
            System.out.println("Error: el mecanico no existe");
        }

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-30s %-16s %-16s %-16s%n",
                "DNI",
                "NOMBRE",
                "APELLIDO",
                "CONTRASEÑA",
                "CORREO",
                "TELEFONO",
                "FECHA_INGRESO",
                "SUELDO");

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-30s %-16s %-16s %-16s",
                mecanico.getDni(),
                mecanico.getNombre(),
                mecanico.getApellido(),
                mecanico.getContrasena(),
                mecanico.getCorreo(),
                mecanico.getTelefono(),
                mecanico.getFechaIngreso(),
                mecanico.getSueldo()
        );

        System.out.println("");

    }

}
