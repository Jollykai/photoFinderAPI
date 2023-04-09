package com.jollykai.photoFinderAPI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@Configuration
public class ConnectConfig {
    private String host;
    private int port;
    private String username;
    private String password;

    @Bean
    ConnectConfig connectionConfig(@Value("${ftp.host}") String host,
                                   @Value("${ftp.port}") int port,
                                   @Value("${ftp.username}") String username,
                                   @Value("${ftp.password}") String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        return new ConnectConfig();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}