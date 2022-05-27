FROM openjdk:17-slim

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

COPY build/libs/*SNAPSHOT.jar /opt/service.jar
COPY run.sh /opt/run.sh
RUN chmod +x /opt/run.sh

EXPOSE 8081

CMD [ "/opt/run.sh" ]
