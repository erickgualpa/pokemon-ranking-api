FROM bellsoft/liberica-openjdk-alpine-musl:21
LABEL org.opencontainers.image.authors='egualpam'
COPY target/pokemon-ranking-api-0.0.1-SNAPSHOT.jar pokemon-ranking-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "pokemon-ranking-api-0.0.1-SNAPSHOT.jar"]