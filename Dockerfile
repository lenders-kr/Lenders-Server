FROM maven:3.8.3-amazoncorretto-17
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests
ENTRYPOINT ["java","-jar","target/lenders-0.0.1-SNAPSHOT.jar"]
