package com.publicaciones.publicaciones.controllers;

import com.publicaciones.publicaciones.models.*;
import com.publicaciones.publicaciones.repository.PostRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository repo;

    public PostController(PostRepository repo) {
        this.repo = repo;
    }

    // Obtener todos los posts
    @GetMapping
    public List<Post> getAll() {
        return repo.findAll();
    }

    // Obtener un post por su ID
    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));
    }

    // Obtener comentarios de un post
    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@PathVariable int id) {
        try {
            return repo.getCommentsByPost(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Obtener resumen de ratings de un post
    @GetMapping("/{id}/rating/avg")
    public RatingSummary getRatingAvg(@PathVariable int id) {
        try {
            return repo.getRatingSummary(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Obtener posts más valorados
    @GetMapping("/top-rated")
    public List<Map<String, Object>> topRated(@RequestParam(defaultValue = "3") int limit) {
        return repo.topRated(limit);
    }

    // Buscar posts por rango de fechas
    @GetMapping("/by-range")
    public List<Post> byRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from))
            throw new IllegalArgumentException("'from' debe ser <= 'to'");
        return repo.postsByRange(from, to);
    }

    // Buscar posts por texto
    @GetMapping("/search")
    public List<Post> search(@RequestParam("query") String query) {
        if (query == null || query.isBlank())
            throw new IllegalArgumentException("query requerido");
        return repo.searchPosts(query);
    }

    // Guarda
    @PostMapping
    public Post guardar(@RequestBody Post post) {
        return repo.savePost(post);
    }

    // Actualiza
    @PutMapping("/{id}")
    public Post actualizar(@PathVariable int id, @RequestBody Post updated) {
        return repo.findById(id).map(existing -> {
            // actualizar campos permitidos
            if (updated.getTitle() != null)
                existing.setTitle(updated.getTitle());
            if (updated.getContent() != null)
                existing.setContent(updated.getContent());
            if (updated.getCreatedAt() != null)
                existing.setCreatedAt(updated.getCreatedAt());
            return repo.save(existing);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));
    }

    // Elimina
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado");
        }
        repo.deleteById(id);
    }

}