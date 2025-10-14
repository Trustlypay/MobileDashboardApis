# Use Java 21 JDK to compile
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy project files
COPY . .

# Build the project (skip tests if you want faster builds)
RUN ./mvnw clean package -DskipTests

# Use a smaller JRE image for runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
