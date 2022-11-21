FROM openjdk:8
COPY . src/java
WORKDIR /src/java
RUN ["javac","javasample.java"]
ENTRYPOINT ["java","javasample"]