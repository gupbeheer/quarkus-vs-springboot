FROM openjdk:17-slim

ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

# Install java and the run-java script
# Also set up permissions for user `1001`
RUN mkdir /deployments \
    && chown 1001 /deployments \
    && chmod "g+rwX" /deployments \
    && chown 1001:root /deployments
COPY run.sh /deployments/run.sh
RUN chown 1001 /deployments/run.sh \
    && chmod 540 /deployments/run.sh

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=1001 build/quarkus-app/lib/ /deployments/lib/
COPY --chown=1001 build/quarkus-app/*.jar /deployments/
COPY --chown=1001 build/quarkus-app/app/ /deployments/app/
COPY --chown=1001 build/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 1001

ENTRYPOINT [ "/deployments/run.sh" ]

