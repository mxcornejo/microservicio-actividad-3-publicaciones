package com.publicaciones.publicaciones.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.publicaciones.publicaciones.models.Comment;
import com.publicaciones.publicaciones.models.Post;
import com.publicaciones.publicaciones.models.RatingSummary; // { changed code }

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // Cambiado: seleccionar comentarios desde la entidad Comment (no depende de una
    // colección en Post)
    @Query("select c from Comment c where c.postId = :postId")
    List<Comment> getCommentsByPost(@Param("postId") int postId);

    // Cambiado: calcular resumen de ratings usando la entidad Rating directamente
    @Query("select new com.publicaciones.publicaciones.models.RatingSummary(avg(r.value), count(r)) " +
            "from Rating r where r.postId = :postId group by r.postId")
    RatingSummary getRatingSummary(@Param("postId") int postId); // { changed code }

    // Posts en un rango de fechas (asume campo createdAt de tipo
    // LocalDate/LocalDateTime en Post)
    @Query("select p from Post p where p.createdAt between :from and :to")
    List<Post> postsByRange(@Param("from") LocalDate from, @Param("to") LocalDate to); // { changed code }

    // Búsqueda por texto en título o contenido
    @Query("select p from Post p where lower(p.title) like lower(concat('%', :query, '%')) " +
            "or lower(p.content) like lower(concat('%', :query, '%'))")
    List<Post> searchPosts(@Param("query") String query); // { changed code }

    // Top rated - ajustar consulta nativa para Oracle (FETCH FIRST ...)
    @Query(value = "SELECT p.id as post_id, p.title as title, AVG(r.value) as avg_rating " +
            "FROM POSTS p JOIN RATINGS r ON r.post_id = p.id " +
            "GROUP BY p.id, p.title " +
            "ORDER BY avg_rating DESC " +
            "FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
    List<Map<String, Object>> topRated(@Param("limit") int limit); // { changed code }

    // Wrapper que reutiliza save() de JpaRepository para mantener la llamada
    // savePost desde el controlador
    default Post savePost(Post post) {
        return save(post);
    }
}