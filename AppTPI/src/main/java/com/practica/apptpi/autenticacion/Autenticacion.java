package com.practica.apptpi.autenticacion;

import com.practica.apptpi.dao.UsuarioDAO;
import com.practica.apptpi.modelo.Usuario;

public class Autenticacion {

    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    public static String autenticarUsuario(int dni, String contrasena){
        
        if(usuarioDAO.verificarCredenciales(dni, contrasena)){
            
            Usuario usuario = usuarioDAO.searchByDni(dni);
            
            return usuario.getRol();
            
        }else{
            return null;
        }
        
    }
    
}
