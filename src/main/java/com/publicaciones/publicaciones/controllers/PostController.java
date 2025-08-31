package com.publicaciones.publicaciones.controllers;

import com.publicaciones.publicaciones.models.*;
import com.publicaciones.publicaciones.services.DataService;
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

    private final DataService data;

    public PostController(DataService data) { this.data = data; }

    @GetMapping
    public List<Post> getAll() { return data.getPosts(); }

    @GetMapping("/{id}")
    public Post getById(@PathVariable int id) {
        return data.findPost(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@PathVariable int id) {
        try { return data.getCommentsByPost(id); }
        catch (NoSuchElementException e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()); }
    }

    @GetMapping("/{id}/rating/avg")
    public RatingSummary getRatingAvg(@PathVariable int id) {
        try { return data.getRatingSummary(id); }
        catch (NoSuchElementException e) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()); }
    }

    @GetMapping("/top-rated")
    public List<Map<String, Object>> topRated(@RequestParam(defaultValue = "3") int limit) {
        return data.topRated(limit);
    }

    @GetMapping("/by-range")
    public List<Post> byRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from)) throw new IllegalArgumentException("'from' debe ser <= 'to'");
        return data.postsByRange(from, to);
    }

    @GetMapping("/search")
    public List<Post> search(@RequestParam("query") String query) {
        if (query == null || query.isBlank()) throw new IllegalArgumentException("query requerido");
        return data.searchPosts(query);
    }
}