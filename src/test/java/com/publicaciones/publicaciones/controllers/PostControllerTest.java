package com.publicaciones.publicaciones.controllers;

import com.publicaciones.publicaciones.models.Post;
import com.publicaciones.publicaciones.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostRepository repo;

    private Post sample;

    @BeforeEach
    void setup() {
        sample = new Post("Titulo demo", "Contenido demo", LocalDate.now());
        sample.setId(1);
    }

    @Test
    void getById_found_returnsHateoas() throws Exception {
        Mockito.when(repo.findById(1)).thenReturn(Optional.of(sample));

        mvc.perform(get("/api/posts/1").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.title").value("Titulo demo"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.comments.href").exists());
    }

    @Test
    void getAll_returnsCollection() throws Exception {
        Mockito.when(repo.findAll()).thenReturn(List.of(sample));

        mvc.perform(get("/api/posts").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        Mockito.when(repo.findById(anyInt())).thenReturn(Optional.empty());

        mvc.perform(get("/api/posts/999").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());
    }
}
