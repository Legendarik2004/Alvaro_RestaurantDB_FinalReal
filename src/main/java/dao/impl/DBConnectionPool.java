package dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.Configuration;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


@Singleton
public class DBConnectionPool {

    private final Configuration config;
    private final DataSource hikariDataSource;


    @Inject
    public DBConnectionPool(Configuration config) {
        this.config = config;
        hikariDataSource = getHikariPool();
    }

    private DataSource getHikariPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getProperty("urlDB"));
        hikariConfig.setUsername(config.getProperty("user_name"));
        hikariConfig.setPassword(config.getProperty("password"));
        hikariConfig.setDriverClassName(config.getProperty("driver"));
        hikariConfig.setMaximumPoolSize(4);

        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = hikariDataSource.getConnection();
        } catch (SQLException e) {
            Logger.getLogger(DBConnectionPool.class.getName()).log(Level.SEVERE, null, e);
        }

        return con;
    }

    @PreDestroy
    public void closePool() {
        ((HikariDataSource) hikariDataSource).close();
    }

}
