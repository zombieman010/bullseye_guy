FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.0.3_7

MAINTAINER Nathaniel Pautzke

RUN mkdir -p /user/share/bullseye-guy/bin
RUN mkdir -p /user/share/bullseye-guy/bin/config

ADD /target/bullseye-guy*SNAPSHOT.jar /user/share/bullseye-guy/bin/bullseye-guy.jar

EXPOSE 8080

WORKDIR /user/share/bullseye-guy/bin/

ENTRYPOINT ["/opt/java/openjdk/bin/java", "-jar", "bullseye-guy.jar"]