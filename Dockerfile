FROM openjdk:21-jdk

COPY build/libs/blue-harvest.jar /home/java/

WORKDIR /home/java
CMD ["java", "-jar", "blue-harvest.jar"]



