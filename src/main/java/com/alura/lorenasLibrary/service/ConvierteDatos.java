package com.alura.lorenasLibrary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
            if (json == null || json.trim().isEmpty()) {
                throw new RuntimeException("El JSON es nulo o está vacío.");
            }
            try{
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar JSON: " + e.getMessage(), e);
        }
    }
}
