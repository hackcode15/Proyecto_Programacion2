package com.practica.apptpi.controladores;

import com.practica.apptpi.dao.ClienteDAO;
import com.practica.apptpi.modelo.Cliente;
import com.practica.apptpi.modelo.Usuario;
import java.util.*;

public class ClienteControlador {

    private ClienteDAO clienteDAO;
    private Scanner sc;

    public ClienteControlador() {
        clienteDAO = new ClienteDAO();
        sc = new Scanner(System.in);
    }

    // usu del metodo: create
    public void registrarseComoCliente() {

        System.out.println("== REGISTRATE COMO CLIENTE ==");

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
    public void modificarMisDatos(Usuario usuarioActual) {

        if (!usuarioActual.getRol().equalsIgnoreCase("Cliente")) {
            System.out.println("Error: no tienes accceso para modificar cliente");
            return;
        }

        System.out.println("== ACTUALIZA TUS DATOS ==");

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
    public void verDatos(Usuario usuarioActual) {

        if (usuarioActual.getRol().equalsIgnoreCase("Mecanico")) {

            System.out.println("== OBTENER INFORMACION DEL CLIENTE ==");

            // Capturo el posible error de entrada en dni, capturando la excepcion
            boolean entradaCorrecta = false;
            int dni = 0;
            do{
                
                try {

                    System.out.print("Digite el DNI: ");
                    dni = sc.nextInt();

                    entradaCorrecta = true;

                } catch (InputMismatchException e) {
                    System.out.println("Error: debes introducir un valor numerico");
                    sc.nextLine();
                }

            }while(!entradaCorrecta);

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
