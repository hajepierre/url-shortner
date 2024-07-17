package com.hajepierre.urlshortner.controllers;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hajepierre.urlshortner.dtos.UrlModel;
import com.hajepierre.urlshortner.services.UrlShortnerService;
import redis.embedded.RedisServerBuilder;

@WebMvcTest(controllers = ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ApplicationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlShortnerService service;

    private UrlModel obj = new UrlModel("1234", 60L, "https://example.com");

    private redis.embedded.RedisServer redisServer;

    @BeforeAll
    public void startRedisServer() {
        redisServer = new RedisServerBuilder().port(6380).setting("maxmemory 256M").build();
        redisServer.start();

        try {
            service.registerUrl(obj);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @AfterAll
    public void stopRedisServer() {
        redisServer.stop();
    }

    @Test
    public void testUrlRegistrationWithDuplicateId() throws Exception {
        ResultActions response = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)));

        response.andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    public void testUrlRegistrationWithMalformedObject() throws Exception {
        UrlModel obj = new UrlModel("1", 60L, "url");
        ResultActions response = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUrlRegistrationSucessfully() throws Exception {
        UrlModel obj = new UrlModel("2", 60L, "https://example.com");

        ResultActions response = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(obj)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status",
                        CoreMatchers.is("SUCCESS")));
    }

    @Test
    public void testUnregisterUrlWithMissingId() throws Exception {
        ResultActions response = mockMvc.perform(delete("/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUnregisterUrlSuccessfully() throws Exception {
        ResultActions response = mockMvc.perform(delete("/1234")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUrlResoltionWithMissingId() throws Exception {
        ResultActions response = mockMvc.perform(get("/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUrlResoltion() throws Exception {
        ResultActions response = mockMvc.perform(get("/1234")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isTemporaryRedirect());
    }

}
