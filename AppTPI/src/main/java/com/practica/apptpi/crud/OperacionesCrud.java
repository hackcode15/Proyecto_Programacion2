package com.practica.apptpi.crud;

import java.util.List;

public interface OperacionesCrud <T>{
    
    boolean create(T entidad);
    List<T> read();
    boolean update(T entidad);
    boolean delete(T entidad);
    T searchByDni(int dni);
    
}
