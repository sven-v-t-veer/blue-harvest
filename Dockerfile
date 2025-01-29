FROM openjdk:21-jdk

COPY build/libs/blue-harvest.jar /home/java/
COPY run.sh /home/java/

WORKDIR /home/java
ENTRYPOINT ["./run.sh"]



