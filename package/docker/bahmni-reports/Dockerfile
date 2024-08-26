FROM amazoncorretto:11

ENV SERVER_PORT=8051
ENV BASE_DIR=/var/run/bahmni-reports
ENV CONTEXT_PATH=/bahmnireports
ENV WAR_DIRECTORY=/var/run/bahmni-reports/bahmni-reports
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -Dsun.net.client.defaultConnectTimeout=600000 -Dsun.net.client.defaultReadTimeout=600000"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8003,server=y,suspend=n"

RUN mkdir -p /var/log/bahmni-reports
RUN mkdir -p /home/bahmni/reports
RUN mkdir -p ${WAR_DIRECTORY}
RUN mkdir -p ${HOME}/.bahmni-reports/
RUN mkdir -p /var/log/bahmni-reports/
RUN yum install -y gettext unzip

ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for /etc/wait-for
RUN yum install -y nc \
    elfutils-libelf-0.176-2.amzn2.0.1 \
    libnghttp2-1.41.0-1.amzn2.0.1

RUN chmod +x /etc/wait-for

COPY target/bahmnireports.war /etc/bahmni-reports/bahmnireports.war
RUN cd ${WAR_DIRECTORY} && jar xvf /etc/bahmni-reports/bahmnireports.war

ADD https://repo.mybahmni.org/packages/build/bahmni-embedded-tomcat-8.0.42.jar /opt/bahmni-reports/lib/bahmni-embedded-tomcat.jar

COPY package/resources/log4j2.properties ${WAR_DIRECTORY}/WEB-INF/classes/
COPY package/resources/run-liquibase.sh /etc/bahmni-reports/
COPY package/docker/bahmni-reports/template/bahmni-reports.properties.template /etc/bahmni-reports/

COPY package/docker/bahmni-reports/start.sh start.sh
RUN chmod +x start.sh
CMD [ "./start.sh" ]
