package com.publicaciones.publicaciones.controllers;

import com.publicaciones.publicaciones.models.*;
import com.publicaciones.publicaciones.repository.PostRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;

import java.net.URI;

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
    public CollectionModel<EntityModel<Post>> getAll() {
        List<Post> posts = repo.findAll();
        List<EntityModel<Post>> resources = posts.stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PostController.class).getById(p.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).getComments(p.getId())).withRel("comments")))
                .toList();
        return CollectionModel.of(resources, linkTo(methodOn(PostController.class).getAll()).withSelfRel());
    }

    // Obtener un post por su ID
    @GetMapping("/{id}")
    public EntityModel<Post> getById(@PathVariable int id) {
        Post p = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));
        return EntityModel.of(p,
                linkTo(methodOn(PostController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(PostController.class).getComments(id)).withRel("comments"),
                linkTo(methodOn(PostController.class).getRatingAvg(id)).withRel("rating"),
                linkTo(methodOn(PostController.class).actualizar(id, (Post) null)).withRel("update"),
                linkTo(methodOn(PostController.class).eliminar(id)).withRel("delete"));
    }

    // Obtener comentarios de un post
    @GetMapping("/{id}/comments")
    public CollectionModel<EntityModel<Comment>> getComments(@PathVariable int id) {
        try {
            List<Comment> comments = repo.getCommentsByPost(id);
            List<EntityModel<Comment>> resources = comments.stream()
                    .map(c -> EntityModel.of(c,
                            linkTo(methodOn(PostController.class).getComments(id)).withSelfRel(),
                            linkTo(methodOn(PostController.class).getById(id)).withRel("post")))
                    .toList();
            return CollectionModel.of(resources,
                    linkTo(methodOn(PostController.class).getComments(id)).withSelfRel(),
                    linkTo(methodOn(PostController.class).getById(id)).withRel("post"));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Obtener resumen de ratings de un post
    @GetMapping("/{id}/rating/avg")
    public EntityModel<RatingSummary> getRatingAvg(@PathVariable int id) {
        try {
            RatingSummary rs = repo.getRatingSummary(id);
            return EntityModel.of(rs,
                    linkTo(methodOn(PostController.class).getRatingAvg(id)).withSelfRel(),
                    linkTo(methodOn(PostController.class).getById(id)).withRel("post"));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Obtener posts más valorados
    @GetMapping("/top-rated")
    public CollectionModel<EntityModel<Map<String, Object>>> topRated(@RequestParam(defaultValue = "3") int limit) {
        List<Map<String, Object>> list = repo.topRated(limit);
        List<EntityModel<Map<String, Object>>> resources = list.stream().map(m -> {
            EntityModel<Map<String, Object>> em = EntityModel.of(m);
            Object pid = m.get("post_id");
            if (pid instanceof Number) {
                int id = ((Number) pid).intValue();
                em.add(linkTo(methodOn(PostController.class).getById(id)).withRel("post"));
            }
            return em;
        }).toList();
        return CollectionModel.of(resources, linkTo(methodOn(PostController.class).topRated(limit)).withSelfRel());
    }

    // Buscar posts por rango de fechas
    @GetMapping("/by-range")
    public CollectionModel<EntityModel<Post>> byRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to.isBefore(from))
            throw new IllegalArgumentException("'from' debe ser <= 'to'");
        List<Post> posts = repo.postsByRange(from, to);
        List<EntityModel<Post>> resources = posts.stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PostController.class).getById(p.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).getComments(p.getId())).withRel("comments")))
                .toList();
        return CollectionModel.of(resources, linkTo(methodOn(PostController.class).byRange(from, to)).withSelfRel());
    }

    // Buscar posts por texto
    @GetMapping("/search")
    public CollectionModel<EntityModel<Post>> search(@RequestParam("query") String query) {
        if (query == null || query.isBlank())
            throw new IllegalArgumentException("query requerido");
        List<Post> posts = repo.searchPosts(query);
        List<EntityModel<Post>> resources = posts.stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PostController.class).getById(p.getId())).withSelfRel(),
                        linkTo(methodOn(PostController.class).getComments(p.getId())).withRel("comments")))
                .toList();
        return CollectionModel.of(resources, linkTo(methodOn(PostController.class).search(query)).withSelfRel());
    }

    // Guarda
    @PostMapping
    public EntityModel<Post> guardar(@RequestBody Post post) {
        Post saved = repo.savePost(post);
        return EntityModel.of(saved,
                linkTo(methodOn(PostController.class).getById(saved.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).getAll()).withRel("posts"));
    }

    // Actualiza
    @PutMapping("/{id}")
    public EntityModel<Post> actualizar(@PathVariable int id, @RequestBody Post updated) {
        Post saved = repo.findById(id).map(existing -> {
            // actualizar campos permitidos
            if (updated.getTitle() != null)
                existing.setTitle(updated.getTitle());
            if (updated.getContent() != null)
                existing.setContent(updated.getContent());
            if (updated.getCreatedAt() != null)
                existing.setCreatedAt(updated.getCreatedAt());
            return repo.save(existing);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado"));
        return EntityModel.of(saved,
                linkTo(methodOn(PostController.class).getById(saved.getId())).withSelfRel(),
                linkTo(methodOn(PostController.class).getAll()).withRel("posts"));
    }

    // Elimina
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no encontrado");
        }
        repo.deleteById(id);
        URI uri = linkTo(methodOn(PostController.class).getAll()).toUri();
        return ResponseEntity.noContent().location(uri).build();
    }

}