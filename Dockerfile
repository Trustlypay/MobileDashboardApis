# Use OpenJDK 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

COPY src ./src

# Build
RUN ./mvnw clean package -DskipTests

# Copy jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 10000

# Start (Render will set $PORT)
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-10000}"]
