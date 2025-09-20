package com.intraron.r2dbc.config;

import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import io.r2dbc.postgresql.api.PostgresqlStatement;
import io.r2dbc.postgresql.codec.Codec;
import io.r2dbc.postgresql.codec.CodecRegistry;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

// Convertidor para leer de la base de datos
public class RolesReadConverter implements Converter<String[], List<String>> {
    @Override
    public List<String> convert(String[] source) {
        return Arrays.asList(source);
    }
}

