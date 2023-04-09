package com.jollykai.photoFinderAPI.entity;

import java.util.Date;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
public class Photo {
    private String path;
    private Date creationDate;
    private Long size;

    public Photo() {
    }

    public Photo(String path, Date creationDate, Long size) {
        this.path = path;
        this.creationDate = creationDate;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
