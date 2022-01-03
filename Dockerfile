FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/employees-0.0.1-SNAPSHOT.jar /home/employees-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/home/employees-0.0.1-SNAPSHOT.jar"]