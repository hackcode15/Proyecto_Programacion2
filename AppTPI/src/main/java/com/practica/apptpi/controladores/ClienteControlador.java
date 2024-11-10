package com.practica.apptpi.controladores;

import com.practica.apptpi.dao.ClienteDAO;
import com.practica.apptpi.modelo.Cliente;
import com.practica.apptpi.modelo.Usuario;
import java.util.*;

// TERMINADO
public class ClienteControlador {

    private ClienteDAO clienteDAO;
    private Scanner sc;

    public ClienteControlador() {
        clienteDAO = new ClienteDAO();
        sc = new Scanner(System.in);
    }

    /*
    PERMISOS DE CLIENTES
    registrarse
    ver los datos de su cuenta
    modificar los datos de su cuenta
    eliminar su propia cuenta
     */
 /*
    PERMISOS DE MECANICO
    ver la lista completa de clientes (con restrincion de datos personales)
    buscar un cliente en especifico y ver sus datos (con restrincion de datos personales)
     */
    // usu del metodo: create
    public void registrarseComoCliente() {

        System.out.println("== REGISTRATE COMO CLIENTE ==");

        System.out.print("Digite su DNI: ");
        int dni = sc.nextInt();

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

        // Opcional
        System.out.print("Domicilio (enter para omitir): ");
        String domicilio = sc.nextLine();

        System.out.print("Regimen Laboral: ");
        String regimen = sc.nextLine();

        Cliente clienteNuevo = Cliente.builder()
                .dni(dni)
                .nombre(nombre)
                .apellido(apellido)
                .contrasena(contrasena)
                .correo(correo)
                .telefono(telefono)
                .domicilio(domicilio)
                .RegimenLaboral(regimen)
                .rol("Cliente")
                .build();

        if (clienteDAO.create(clienteNuevo)) {
            System.out.println("\"" + clienteNuevo.getNombre() + "\" has creado tu cuenta de Cliente correctamente!!!");
            System.out.println("!AHORA INICIA SESION CON TU CUENTA¡");
        }

    }

    // uso del metodo: read
    // SOLO LOS MECANICOS PUEDEN VER LA LISTA COMPLETA DE CLIENTES
    public void listarClientes(Usuario usuarioActual) {

        if (usuarioActual.getRol().equalsIgnoreCase("Mecanico")) {

            System.out.println("== TODOS LOS CLIENTES ==");

            List<Cliente> listaClientes = clienteDAO.read();

            // formato de impresion
            System.out.printf(
                    "%-16s %-16s %-30s %-16s %-16s %-30s %-16s%n",
                    "NOMBRE",
                    "APELLIDO",
                    "CORREO",
                    "TELEFONO",
                    "FECHA_INGRESO",
                    "DOMICILIO",
                    "REGIMEN");

            // expresiones lambda
            listaClientes.stream().
                    map(p -> String.format(
                    "%-16s %-16s %-30s %-16s %-16s %-30s %-16s",
                    p.getNombre(),
                    p.getApellido(),
                    p.getCorreo(),
                    p.getTelefono(),
                    p.getFechaIngreso(),
                    p.getDomicilio(),
                    p.getRegimenLaboral()))
                    .forEach(System.out::println);

            System.out.println("");

        } else {
            System.out.println("Acceso denegado");
        }

    }

    // usu del metodo: update
    // Un mecanico no puede tener acceso a este metodo
    // SOLO LOS CLIENTES PUEDEN MODIFICAR SUS DATOS
    // EL CLIENTE ACTUAL LOGUEADO SOLO PUEDE MODIFICAR SU CUENTA
    public void modificarMisDatos(Usuario usuarioActual) {

        // Manejo de error
        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Error: no tienes accceso para modificar cliente");
            return;
        }

        System.out.println("== ACTUALIZA TUS DATOS ==");

        /*System.out.print("Digite su DNI: ");
        int dni = sc.nextInt();*/
        // Manejo de error
        /*if (dni != usuarioActual.getDni()) {
            System.out.println("No puedes modificar datos de otro cliente");
            return;
        }*/
        //sc.nextLine();
        //Cliente cliente = clienteDAO.searchByDni(dni);
        // Manejo de error
        /*if (cliente == null) {
            System.out.println("Error: el cliente no existe");
            return;
        }*/
        Cliente cliente = clienteDAO.searchByDni(usuarioActual.getDni());

        System.out.println("Hola \"" + cliente.getNombre() + "\", estas por actualizar tus datos");

        System.out.print("Nueva contraseña: ");
        String nuevaContrasena = sc.nextLine();

        System.out.print("Nuevo Telefono: ");
        String nuevaTelefono = sc.nextLine();

        System.out.print("Nuevo Correo: ");
        String nuevaCorreo = sc.nextLine();

        System.out.print("Nueva Domicilio: ");
        String nuevaDomicilio = sc.nextLine();

        cliente.setContrasena(nuevaContrasena);
        cliente.setTelefono(nuevaTelefono);
        cliente.setCorreo(nuevaCorreo);
        cliente.setDomicilio(nuevaDomicilio);

        if (clienteDAO.update(cliente)) {
            System.out.println("\"" + cliente.getNombre() + "\" has actualizado correctamente tus datos");
        }

    }

    // usu del metodo: delete
    // NO TIENEN ACCESO LOS MECANICOS A ESTE METODO
    // SOLO LOS CLIENTES PUEDEN ELIMINAR SU CUENTA
    // EL CLIENTE ACTUAL LOGUEADO SOLO PUEDE ELIMINAR SU CUENTA
    public void eliminarMiCuenta(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Error: no tienes acceso");
            return;
        }

        Cliente cliente = clienteDAO.searchByDni(usuarioActual.getDni());

        if (clienteDAO.delete(cliente)) {
            System.out.println("Cliente \"" + cliente.getNombre() + "\" has eliminado tu cuenta correctamente");
        }

    }

    // usu del metodo: searchByDni
    // TANTO LOS CLIENTES COMO LOS MECANICOS TIENEN ACCESO A ESTE METODO
    // CON RESTRINCIONES DE DATOS, DEPENDIENDO EL ROL DEL USUARIO LOGUEADO
    public void verDatos(Usuario usuarioActual) {

        if (usuarioActual.getRol().equalsIgnoreCase("Mecanico")) {

            System.out.println("== OBTENER INFORMACION DEL CLIENTE ==");
            System.out.print("Digite su DNI: ");
            int dni = sc.nextInt();

            Cliente cliente = clienteDAO.searchByDni(dni);

            if (cliente == null) {
                System.out.println("Error: el cliente no existe");
                return;
            }

            dameDatosResumidos(cliente);
        } else if (usuarioActual.getRol().equalsIgnoreCase("Cliente")) {

            System.out.println("== INFORMACION DE TU CUENTA ==");

            Cliente cliente = clienteDAO.searchByDni(usuarioActual.getDni());

            dameDatosCompletos(cliente);

        }

    }

    // METODOS ESPECIFICOS
    // listar todos los datos del cliente
    private void dameDatosCompletos(Cliente cliente) {

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-30s %-16s %-30s %-16s%n",
                "DNI",
                "NOMBRE",
                "APELLIDO",
                "CONTRASEÑA",
                "CORREO",
                "TELEFONO",
                "DOMICILIO",
                "REGIMEN");

        System.out.printf(
                "%-16s %-16s %-16s %-16s %-30s %-16s %-30s %-16s",
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getContrasena(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cliente.getDomicilio(),
                cliente.getRegimenLaboral()
        );

        System.out.println("");

    }

    private void dameDatosResumidos(Cliente cliente) {

        System.out.printf(
                "%-16s %-16s %-16s %-30s %-16s %-16s %-30s %-16s%n",
                "DNI",
                "NOMBRE",
                "APELLIDO",
                "CORREO",
                "TELEFONO",
                "FECHA_INGRESO",
                "DOMICILIO",
                "REGIMEN");

        System.out.printf(
                "%-16s %-16s %-16s %-30s %-16s %-16s %-30s %-16s",
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cliente.getFechaIngreso(),
                cliente.getDomicilio(),
                cliente.getRegimenLaboral()
        );

        System.out.println("");

    }

}
