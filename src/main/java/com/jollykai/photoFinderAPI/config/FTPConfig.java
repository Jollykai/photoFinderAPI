package com.jollykai.photoFinderAPI.config;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@Configuration
public class FTPConfig {

    @Bean
    FTPClient ftpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("UTF-8");
        return ftpClient;
    }

}