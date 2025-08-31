# Microservicio Actividad 3 Publicaciones

Microservicio solicitado para publicaciones, actividad fullstack 1 Duoc.

## Curls solicitados

### Lista de posts

curl -s http://localhost:8080/api/posts | jq

### Detalle de un post

curl -s http://localhost:8080/api/posts/1 | jq

### Comentarios del post 1

curl -s http://localhost:8080/api/posts/1/comments | jq

### Promedio de calificaciones del post 1

curl -s http://localhost:8080/api/posts/1/rating/avg | jq

### Top posts por rating

curl -s "http://localhost:8080/api/posts/top-rated?limit=3" | jq

### Posts por rango de fechas

curl -s "http://localhost:8080/api/posts/by-range?from=2025-08-25&to=2025-08-31" | jq

### Búsqueda por texto en título o contenido

curl -s "http://localhost:8080/api/posts/search?query=mascota" | jq
