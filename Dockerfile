FROM openjdk:11-jdk-slim
COPY target/demo-hazelcast-0.0.1-SNAPSHOT.jar /hazelcast/
EXPOSE 8082

RUN apt-get update -y
RUN apt-get install net-tools -y
#RUN apt-get install iproute2 -y
RUN apt-get install iputils-ping -y

ENTRYPOINT java -jar /hazelcast/demo-hazelcast-0.0.1-SNAPSHOT.jar
