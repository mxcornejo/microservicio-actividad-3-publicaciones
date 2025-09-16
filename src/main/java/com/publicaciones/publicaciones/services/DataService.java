// package com.publicaciones.publicaciones.services;

// import com.publicaciones.publicaciones.models.*;
// import jakarta.annotation.PostConstruct;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.*;
// import java.util.concurrent.CopyOnWriteArrayList;
// import java.util.stream.Collectors;

// @Service
// public class DataService {

// private final List<Post> posts = new CopyOnWriteArrayList<>();
// private final List<Comment> comments = new CopyOnWriteArrayList<>();
// private final List<Rating> ratings = new CopyOnWriteArrayList<>();

// private int nextPostId = 1;
// private int nextCommentId = 1;
// private int nextRatingId = 1;

// @PostConstruct
// void seed() {
// int p1 = addPost("Primer post: cuidados de tu mascota", "Contenido A",
// LocalDate.now().minusDays(3));
// int p2 = addPost("Segundo post: alimentación saludable", "Contenido B",
// LocalDate.now().minusDays(2));
// int p3 = addPost("Tercer post: juegos y enriquecimiento", "Contenido C",
// LocalDate.now().minusDays(1));

// addComment(p1, "Ana", "¡Muy útil!", LocalDate.now().minusDays(2));
// addComment(p1, "Luis", "Me sirvió bastante.", LocalDate.now().minusDays(1));
// addComment(p2, "Paty", "Gracias por los tips", LocalDate.now());

// addRating(p1, 5);
// addRating(p1, 4);
// addRating(p2, 3);
// addRating(p2, 4);
// addRating(p3, 5);
// }

// // Posts
// public List<Post> getPosts() { return posts; }

// public Optional<Post> findPost(int id) {
// return posts.stream().filter(p -> p.getId() == id).findFirst();
// }

// public int addPost(String title, String content, LocalDate date) {
// if (title == null || title.isBlank()) throw new
// IllegalArgumentException("title requerido");
// if (content == null || content.isBlank()) throw new
// IllegalArgumentException("content requerido");
// if (date == null) throw new IllegalArgumentException("date requerido");
// Post p = new Post(nextPostId++, title, content, date);
// posts.add(p); return p.getId();
// }

// public List<Post> postsByRange(LocalDate from, LocalDate to) {
// return posts.stream()
// .filter(p -> !p.getCreatedAt().isBefore(from) &&
// !p.getCreatedAt().isAfter(to))
// .collect(Collectors.toList());
// }

// public List<Post> searchPosts(String query) {
// String q = query.toLowerCase();
// return posts.stream()
// .filter(p -> p.getTitle().toLowerCase().contains(q) ||
// p.getContent().toLowerCase().contains(q))
// .collect(Collectors.toList());
// }

// // Comments
// public List<Comment> getCommentsByPost(int postId) {
// ensurePostExists(postId);
// return comments.stream().filter(c -> c.getPostId() ==
// postId).collect(Collectors.toList());
// }

// public int addComment(int postId, String author, String text, LocalDate date)
// {
// ensurePostExists(postId);
// if (author == null || author.isBlank()) throw new
// IllegalArgumentException("author requerido");
// if (text == null || text.isBlank()) throw new IllegalArgumentException("text
// requerido");
// if (date == null) throw new IllegalArgumentException("date requerido");
// Comment c = new Comment(nextCommentId++, postId, author, text, date);
// comments.add(c); return c.getId();
// }

// // Ratings
// public List<Rating> getRatingsByPost(int postId) {
// ensurePostExists(postId);
// return ratings.stream().filter(r -> r.getPostId() ==
// postId).collect(Collectors.toList());
// }

// public int addRating(int postId, int value) {
// ensurePostExists(postId);
// if (value < 1 || value > 5) throw new IllegalArgumentException("value debe
// estar entre 1 y 5");
// Rating r = new Rating(nextRatingId++, postId, value);
// ratings.add(r); return r.getId();
// }

// public RatingSummary getRatingSummary(int postId) {
// ensurePostExists(postId);
// List<Rating> list = getRatingsByPost(postId);
// int count = list.size();
// double avg = count == 0 ? 0.0 :
// list.stream().mapToInt(Rating::getValue).average().orElse(0.0);
// return new RatingSummary(postId, round1(avg), count);
// }

// public List<Map<String, Object>> topRated(int limit) {
// if (limit <= 0) throw new IllegalArgumentException("limit > 0 requerido");
// return posts.stream()
// .map(p -> {
// RatingSummary rs = getRatingSummary(p.getId());
// Map<String, Object> obj = new LinkedHashMap<>();
// obj.put("post", p);
// obj.put("average", rs.getAverage());
// obj.put("count", rs.getCount());
// return obj;
// })
// .sorted((a, b) -> {
// int cmp = Double.compare((double)b.get("average"), (double)a.get("average"));
// if (cmp != 0) return cmp;
// return Integer.compare((int)b.get("count"), (int)a.get("count"));
// })
// .limit(limit)
// .collect(Collectors.toList());
// }

// private double round1(double v) { return Math.round(v * 10.0) / 10.0; }

// private void ensurePostExists(int postId) {
// if (findPost(postId).isEmpty()) {
// throw new NoSuchElementException("Post no encontrado: " + postId);
// }
// }
// }