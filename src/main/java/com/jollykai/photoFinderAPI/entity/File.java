package com.jollykai.photoFinderAPI.entity;

import org.springframework.http.HttpHeaders;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
public class File {
    private byte[] bytes;
    private HttpHeaders httpHeaders;

    public File() {
    }

    public File(byte[] bytes, HttpHeaders httpHeaders) {
        this.bytes = bytes;
        this.httpHeaders = httpHeaders;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
