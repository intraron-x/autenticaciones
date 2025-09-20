package com.intraron.r2dbc.config;

import org.springframework.core.convert.converter.Converter;

import java.util.List;

// Convertidor para escribir en la base de datos
public class RolesWriteConverter implements Converter<List<String>, String[]> {
    @Override
    public String[] convert(List<String> source) {
        return source.toArray(new String[0]);
    }
}
