package com.myshop.api.config;

import com.myshop.api.config.routingdatasource.DataSourceType;
import com.myshop.api.config.routingdatasource.RoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@Profile("prod")
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.MASTER, masterDataSource());
        dataSourceMap.put(DataSourceType.SLAVE, slaveDataSource());

        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        routingDataSource.setTargetDataSources(dataSourceMap);

        return routingDataSource;
    }

    // DependsOn을 넣지 않으면 순환 참조가 발생한다.
    // 의존관계에 있는 1.masterDataSource 2.slaveDataSource 3.routingDataSource 순서로 생성된다.
    @Primary
    @DependsOn({"masterDataSource", "slaveDataSource", "routingDataSource"})
    @Bean
    public DataSource dataSource() {
        // LazyConnectionDataSourceProxy : 필요한 시점에만 커넥션을 점유함으로 분기한 후 선택된 datasource로 커넥션 풀을 점유한다.
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("masterDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }
}
