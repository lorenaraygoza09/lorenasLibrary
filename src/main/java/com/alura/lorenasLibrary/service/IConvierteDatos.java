package com.alura.lorenasLibrary.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class <T> clase);
}
