package org.avangard.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

@Getter
public class DataBase {
    private final HikariConfig config = new HikariConfig();
    private final HikariDataSource ds;

    public Connection getConnection() throws SQLException {
        return getDs().getConnection();
    }

    public DataBase(String jdbcUrl, String userName, String password){
        config.setJdbcUrl(jdbcUrl);
        if(userName != null) config.setUsername(userName);
        if(password != null) config.setPassword(password);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(300);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(10000);

        ds = new HikariDataSource(config);
    }
}
