FROM openjdk:17
EXPOSE 9704
ADD target/ordernotification-0.0.1-SNAPSHOT.jar ordernotification.jar
ENTRYPOINT [ "java","-jar","/ordernotification.jar" ]