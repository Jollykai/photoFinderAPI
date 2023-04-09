package com.jollykai.photoFinderAPI.service;

import com.jollykai.photoFinderAPI.entity.File;
import com.jollykai.photoFinderAPI.entity.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
class PhotoServiceTest {

    @Mock
    private FTPService ftpService;

    @MockBean
    private PhotoService photoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        photoService = new PhotoService(ftpService);
    }

    @Test
    void testGetPhotosList() throws IOException {
        List<Photo> expectedPhotos = Arrays.asList(
                new Photo("photo1.jpg", new Date(1680953692124L), 1024L),
                new Photo("photo2.jpg", new Date(1680953605054L), 2048L)
        );
        when(ftpService.getPhotoList()).thenReturn(expectedPhotos);

        List<Photo> actualPhotos = photoService.getPhotosList();

        assertEquals(expectedPhotos, actualPhotos);
        verify(ftpService).getPhotoList();
    }

    @Test
    void testGetPhotosPathList() throws IOException {
        List<String> expectedPaths = Arrays.asList(
                "/photos/photo1.jpg",
                "/photos/photo2.jpg"
        );
        when(ftpService.getPhotosPathList()).thenReturn(expectedPaths);

        List<String> actualPaths = photoService.getPhotosPathList();

        assertEquals(expectedPaths, actualPaths);
        verify(ftpService).getPhotosPathList();
    }

    @Test
    void testGetPhotoInfo() throws IOException {
        String path = "/photos/photo1.jpg";
        Photo expectedPhoto = new Photo(path, new Date(1680953692124L), 1024L);
        when(ftpService.getPhotoInfo(path)).thenReturn(expectedPhoto);

        Photo actualPhoto = photoService.getPhotoInfo(path);

        assertEquals(expectedPhoto, actualPhoto);
        verify(ftpService).getPhotoInfo(path);
    }

    @Test
    void testDownloadPhoto() throws IOException {
        String path = "/photos/photo1.jpg";
        File expectedFile = new File(new byte[]{0x00, 0x01, 0x02}, new HttpHeaders());
        when(ftpService.downloadPhoto(path)).thenReturn(expectedFile);

        File actualFile = photoService.downloadPhoto(path);

        assertEquals(expectedFile, actualFile);
        verify(ftpService).downloadPhoto(path);
    }

}