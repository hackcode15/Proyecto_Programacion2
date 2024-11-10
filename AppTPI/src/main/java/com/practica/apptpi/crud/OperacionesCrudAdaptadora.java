package com.practica.apptpi.crud;

import java.util.*;

public abstract class OperacionesCrudAdaptadora <T> implements OperacionesCrud <T> {
    
    @Override
    public boolean create(T entidad){
        return false;
    }
    
    @Override
    public List<T> read(){
        return new ArrayList<>();
    }
    
    @Override
    public boolean update(T entidad){
        return false;
    }
    
    @Override
    public boolean delete(T entidad){
        return false;
    }
    
    @Override
    public T searchByDni(int dni){
        return null;
    }
    
}
