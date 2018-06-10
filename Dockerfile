FROM openjdk:8
WORKDIR /opt/
COPY bin/IMS.jar ./app.jar
COPY database/config.cfg ./config.cfg
COPY database/datainventory.csv ./datainventory.csv
COPY database/reportorder.csv ./reportorder.csv
COPY database/reportsales.csv ./reportsales.csv
RUN bash -c 'touch /opt/app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./app.jar"]
