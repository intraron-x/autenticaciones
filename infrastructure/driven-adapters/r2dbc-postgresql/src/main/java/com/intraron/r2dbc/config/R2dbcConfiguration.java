package com.intraron.r2dbc.config;

import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableR2dbcAuditing
public class R2dbcConfiguration {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        List<Object> converters = new ArrayList<>();
        converters.add(new RolesReadConverter());
        converters.add(new RolesWriteConverter());

        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters);
    }

    @Bean
    public NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            public String getColumnName(String propertyName) {
                return propertyName.toLowerCase();
            }
        };
    }
}