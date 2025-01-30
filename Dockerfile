FROM openjdk:21-jdk

COPY build/libs/green-grass.jar /home/java/

WORKDIR /home/java
CMD ["java", "-jar", "green-grass.jar"]



