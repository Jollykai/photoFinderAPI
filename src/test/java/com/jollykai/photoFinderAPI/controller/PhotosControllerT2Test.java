package com.jollykai.photoFinderAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jollykai.photoFinderAPI.entity.File;
import com.jollykai.photoFinderAPI.entity.Photo;
import com.jollykai.photoFinderAPI.service.PhotoService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Project: photoFinderAPI
 *
 * @author Dmitry Istomin aka Jollykai
 * https://github.com/Jollykai
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PhotosControllerT2.class)
public class PhotosControllerT2Test {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PhotoService photoService;

    @Test
    public void testGetPhotos() throws Exception {
        // Mock photoService
        List<String> photos = Arrays.asList("photo1.jpg", "photo2.jpg");
        Mockito.when(photoService.getPhotosPathList()).thenReturn(photos);

        // Perform GET request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/t2/photos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify response
        String response = result.getResponse().getContentAsString();
        Assert.assertEquals("[\"photo1.jpg\",\"photo2.jpg\"]", response);
    }

    @Test
    public void notGetPhotos() throws Exception {
        Mockito.when(photoService.getPhotosPathList()).thenThrow(new IOException(""));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PhotosControllerT2(photoService)).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/t2/photos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    public void testGetPhotoInfo() throws Exception {
        // Mock photoService
        Date creationDate = new Date();
        Photo photoInfo = new Photo("photo1.jpg", creationDate, 1024L);
        Mockito.when(photoService.getPhotoInfo("photo1.jpg")).thenReturn(photoInfo);

        // Perform GET request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/t2/photo").param("path", "photo1.jpg"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify response
        ObjectMapper mapper = new ObjectMapper();
        String response = result.getResponse().getContentAsString();
        Photo actualPhoto = mapper.readValue(response, Photo.class);
        Assert.assertEquals(photoInfo.getPath(), actualPhoto.getPath());
        Assert.assertEquals(photoInfo.getCreationDate(), actualPhoto.getCreationDate());
        Assert.assertEquals(photoInfo.getSize(), actualPhoto.getSize());
    }

    @Test
    public void notGetPhotoInfo() throws Exception {
        Mockito.when(photoService.getPhotoInfo("photo1.jpg")).thenThrow(new IOException(""));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PhotosControllerT2(photoService)).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/t2/photo").param("path", "photo1.jpg")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }

    @Test
    public void testDownloadPhoto() throws Exception {
        // Mock photoService
        byte[] data = "test data".getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        File file = new File(data, headers);
        Mockito.when(photoService.downloadPhoto("photo1.jpg")).thenReturn(file);

        // Perform GET request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/t2/photo/get").param("path", "photo1.jpg"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify response
        byte[] response = result.getResponse().getContentAsByteArray();
        Assert.assertArrayEquals(data, response);
    }

    @Test
    public void notDownloadPhoto() throws Exception {
        Mockito.when(photoService.downloadPhoto("photo1.jpg")).thenThrow(new IOException(""));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PhotosControllerT2(photoService)).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/t2/photo/get").param("path", "photo1.jpg")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }


}