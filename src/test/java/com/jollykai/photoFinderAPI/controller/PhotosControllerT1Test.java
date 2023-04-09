package com.jollykai.photoFinderAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jollykai.photoFinderAPI.entity.Photo;
import com.jollykai.photoFinderAPI.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
@WebMvcTest(PhotosControllerT1.class)
class PhotosControllerT1Test {

    @MockBean
    PhotoService photoService;

    @Test
    void getPhotosList() throws Exception {
        List<Photo> photoList = Arrays.asList(
                new Photo("photo1.jpg", new Date(), 100L),
                new Photo("photo1.jpg", new Date(), 200L)
        );
        Mockito.when(photoService.getPhotosList()).thenReturn(photoList);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PhotosControllerT1(photoService)).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/t1/photos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(photoList)));
    }

    @Test
    void notGetPhotosList() throws Exception {
        Mockito.when(photoService.getPhotosList()).thenThrow(new IOException(""));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PhotosControllerT1(photoService)).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/t1/photos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}