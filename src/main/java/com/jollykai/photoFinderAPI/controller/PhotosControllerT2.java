package com.jollykai.photoFinderAPI.controller;

import com.jollykai.photoFinderAPI.entity.File;
import com.jollykai.photoFinderAPI.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@RestController
public class PhotosControllerT2 {
    private final PhotoService photoService;

    public PhotosControllerT2(@Autowired PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/t2/photos")
    public ResponseEntity<?> getPhotos() {
        try {
            return ResponseEntity.ok(photoService.getPhotosPathList());
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/t2/photo")
    public ResponseEntity<?> getPhotoInfo(@RequestParam("path") String path) {
        try {
            return ResponseEntity.ok(photoService.getPhotoInfo(path));
        } catch (IOException e) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/t2/photo/get")
    public ResponseEntity<?> downloadPhoto(@RequestParam String path) {
        try {
        File file = photoService.downloadPhoto(path);
        return new ResponseEntity<>(file.getBytes(), file.getHttpHeaders(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
