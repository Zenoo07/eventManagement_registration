FROM openjdk:11
EXPOSE 8092
ADD target/registration-1.0.jar registration-1.0.jar
ENTRYPOINT ["java","-jar","/registration-1.0.jar"]