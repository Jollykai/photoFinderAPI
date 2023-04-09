package com.jollykai.photoFinderAPI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@Configuration
public class SearchConfig {
    private String dir;
    private String mask;
    private String root;
    private List<String> ignoredList;

    @Bean
    SearchConfig searchingConfig(
            @Value("${mask.dir}") String dir,
            @Value("${mask.file}") String mask,
            @Value("${mask.root}") String root,
            @Value("${ignoredList}") List<String> ignoredList) {
        this.dir = dir;
        this.mask = mask;
        this.root = root;
        this.ignoredList = ignoredList;
        return new SearchConfig();
    }

    public String getDir() {
        return dir;
    }

    public String getMask() {
        return mask;
    }

    public String getRoot() {
        return root;
    }

    public List<String> getIgnoredList() {
        return ignoredList;
    }
}