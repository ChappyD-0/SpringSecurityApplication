# Docker usage

1. **Construye el jar de la aplicación:**
   ```sh
   ./mvnw clean package
   ```

2. **Construye la imagen Docker (reemplaza `<tu_usuario>` por tu usuario de DockerHub):**
   ```sh
   docker build -t <tu_usuario>/spring-security-app:latest .
   ```

3. **Inicia sesión en DockerHub:**
   ```sh
   docker login
   ```

4. **Sube la imagen a DockerHub:**
   ```sh
   docker push <tu_usuario>/spring-security-app:latest
   ```

5. **(Opcional) Para correr la imagen desde DockerHub:**
   ```sh
   docker run -p 8080:8080 <tu_usuario>/spring-security-app:latest
   ```

6. **Para levantar todo con docker-compose (usando la imagen local):**
   ```sh
   docker-compose up --build
   ```

**Notas:**
- Asegúrate de que tu `application.properties` use las variables de entorno para la base de datos, por ejemplo:
  ```
  spring.datasource.url=${SPRING_DATASOURCE_URL}
  spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
  spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
  ```
- Para insertar datos iniciales (roles, reacciones, etc.), crea un archivo `src/main/resources/data.sql` con los inserts necesarios. Spring Boot ejecutará este script automáticamente al iniciar la aplicación.

