package com.dijicert.booking.config;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@Configuration
public class LiquibaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    private final Environment env;

    public LiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public SpringLiquibase liquibase(
            @Qualifier("taskExecutor") Executor executor,
            LiquibaseProperties liquibaseProperties,
            @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource,
            ObjectProvider<DataSource> dataSource,
            DataSourceProperties dataSourceProperties
    ) {
        SpringLiquibase liquibase = createSpringLiquibase(liquibaseDataSource.getIfAvailable(), liquibaseProperties, dataSource.getIfUnique(), dataSourceProperties);
        liquibase.setChangeLog("classpath:liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setLabelFilter(liquibaseProperties.getLabelFilter());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        log.debug("Configuring Liquibase");
        return liquibase;
    }

    public static SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource, LiquibaseProperties liquibaseProperties, DataSource dataSource, DataSourceProperties dataSourceProperties) {
        DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, liquibaseProperties, dataSource);
        if (liquibaseDataSource != null) {
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setDataSource(liquibaseDataSource);
            return liquibase;
        } else {
            SpringLiquibase liquibase = new DataSourceClosingSpringLiquibase();
            liquibase.setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties));
            return liquibase;
        }
    }

    private static DataSource getDataSource(DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties, DataSource dataSource) {
        if (liquibaseDataSource != null) {
            return liquibaseDataSource;
        } else {
            return liquibaseProperties.getUrl() == null && liquibaseProperties.getUser() == null ? dataSource : null;
        }
    }

    private static DataSource createNewDataSource(LiquibaseProperties liquibaseProperties, DataSourceProperties dataSourceProperties) {
        Objects.requireNonNull(liquibaseProperties);
        Supplier var10000 = liquibaseProperties::getUrl;
        Objects.requireNonNull(dataSourceProperties);
        String url = getProperty(var10000, dataSourceProperties::determineUrl);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getUser;
        Objects.requireNonNull(dataSourceProperties);
        String user = getProperty(var10000, dataSourceProperties::determineUsername);
        Objects.requireNonNull(liquibaseProperties);
        var10000 = liquibaseProperties::getPassword;
        Objects.requireNonNull(dataSourceProperties);
        String password = getProperty(var10000, dataSourceProperties::determinePassword);
        return DataSourceBuilder.create().url(url).username(user).password(password).build();
    }

    private static String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
        return (String) Optional.of(property).map(Supplier::get).orElseGet(defaultValue);
    }
}
