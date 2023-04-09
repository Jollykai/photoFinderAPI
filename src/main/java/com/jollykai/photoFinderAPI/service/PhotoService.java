package com.jollykai.photoFinderAPI.service;

import com.jollykai.photoFinderAPI.entity.File;
import com.jollykai.photoFinderAPI.entity.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@Service
public class PhotoService {
    private final FTPService ftpService;

    public PhotoService(@Autowired FTPService ftpService) {
        this.ftpService = ftpService;
    }

    public List<Photo> getPhotosList() throws IOException {
        return ftpService.getPhotoList();
    }

    public List<String> getPhotosPathList() throws IOException {
        return ftpService.getPhotosPathList();
    }

    public Photo getPhotoInfo(String path) throws IOException {
        return ftpService.getPhotoInfo(path);
    }

    public File downloadPhoto(String path) throws IOException {
        return ftpService.downloadPhoto(path);
    }
}
