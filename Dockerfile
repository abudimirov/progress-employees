FROM openjdk:8-jdk-alpine
# Needed to fix 'Fontconfig warning: ignoring C.UTF-8: not a valid language tag'
ENV LANG en_GB.UTF-8

# JRE fails to load fonts if there are no standard fonts in the image; DejaVu is a good choice,
# see https://github.com/docker-library/openjdk/issues/73#issuecomment-207816707
RUN apk add --update ttf-dejavu && rm -rf /var/cache/apk/*
EXPOSE 8080
ADD target/employees-0.0.2-SNAPSHOT.jar /home/employees-0.0.2-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/home/employees-0.0.2-SNAPSHOT.jar"]