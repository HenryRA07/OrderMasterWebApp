#FROM icr.io/appcafe/open-liberty:full-java21-openj9-ubi-minimal
FROM icr.io/appcafe/open-liberty:kernel-slim-java21-openj9-ubi-minimal

# Argumento con valor por defecto
ARG ENV_DRIVER_JDBC_DIR=./target/lib/*.jar

#Funcionando con docker-compose
COPY --chown=1001:0 ${ENV_DRIVER_JDBC_DIR} /config/

COPY src/main/liberty/config/server.xml /config/server.xml
# This script adds the requested XML snippets to enable Liberty features and grow the image to be fit-for-purpose.
# This option is available only in the 'kernel-slim' image type. The 'full' and 'beta' tags already include all features.
RUN features.sh

COPY target/jbrew-app.war /config/apps/

# This script adds the requested server configuration, applies any interim fixes, and populates caches to optimize the runtime.
RUN configure.sh
