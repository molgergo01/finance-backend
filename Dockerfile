FROM eclipse-temurin:21-jdk-alpine
LABEL authors="mol.gergo01"

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]