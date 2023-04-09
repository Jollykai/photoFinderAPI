package com.jollykai.photoFinderAPI.controller;

import com.jollykai.photoFinderAPI.entity.Photo;
import com.jollykai.photoFinderAPI.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@RestController
public class PhotosControllerT1 {
    private final PhotoService photoService;

    public PhotosControllerT1(@Autowired PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/t1/photos")
    public ResponseEntity<?> getPhotosList() {
        try {
            return ResponseEntity.ok(photoService.getPhotosList());
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
