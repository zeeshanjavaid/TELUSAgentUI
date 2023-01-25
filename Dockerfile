FROM registry.access.redhat.com/jboss-webserver-3/webserver31-tomcat8-openshift

LABEL "created.on"="Wed Oct 7, 2020"
LABEL "owner"="admin@fico.com"
LABEL "DESCRIPTION"="FICO Applications Workbench Base Archetype Image"

# Enabling hot deployment
COPY server.xml /opt/webserver/conf/server.xml

USER root

ENV DMP_DATA_DIR /app-root/runtime/data
ENV DMP_REPO_DIR /app-root/runtime/repo
ENV DMP_TMP_DIR /tmp
ENV DMP_LOG_DIR /opt/webserver/logs
ENV DMP_CONFIG_DIR /app-root/runtime/config
ENV GC_MAX_METASPACE_SIZE 1024
ENV JAVA_OPTIONS -XX:MaxMetaspaceSize=2048m

# Copy the war to webapps folder
COPY target/DMPComponent.war $JWS_HOME/webapps/ROOT.war
COPY target/DMPComponent-archetype/DMP-INF/ $DMP_DATA_DIR/DMP-INF/

RUN chmod -R 777 /app-root/

#ENV JPDA_SUSPEND="y"

EXPOSE 8000