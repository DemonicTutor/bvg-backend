FROM eclipse-temurin:19_36-jdk-alpine

RUN mkdir /opt/app
WORKDIR /opt/app

COPY src/docker/* /opt/app/
COPY target/bvg-backend-fat.jar /opt/app/app.jar

CMD ["/bin/sh", "run.sh"]
