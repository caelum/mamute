FROM java:8-jdk
MAINTAINER Roy Meissner, https://rmeissn.github.io

# Mamute VERSION --> Tested for v1.3 and 1.4
# On change rember to edit the variables also in ../docker-compose.yml
ENV VERSION=1.5.0 USER=mamute PASSWORD=Shei6cea DB=db/mamute_db

RUN mkdir /mamute
WORKDIR /mamute

RUN wget -nv https://github.com/caelum/mamute/releases/download/v${VERSION}/mamute-${VERSION}.war && \
    unzip mamute-${VERSION}.war && \
    rm mamute-${VERSION}.war

ADD ./production.properties /mamute/WEB-INF/classes/production.properties

RUN if echo "$VERSION" | grep -q -E '^1.[345].0$'; then mv /mamute/WEB-INF/classes/production/hibernate.cfg.xml.example /mamute/WEB-INF/classes/production/hibernate.cfg.xml; fi

RUN sed -i -e "s?>root<?>$USER<?g" /mamute/WEB-INF/classes/production/hibernate.cfg.xml && \
    sed -i -e "s?><?>$PASSWORD<?g" WEB-INF/classes/production/hibernate.cfg.xml && \
    sed -i -e "s?localhost/mamute_production?$DB?g" WEB-INF/classes/production/hibernate.cfg.xml

RUN chmod +x run.sh

EXPOSE 8080

# Wait for intial Database init --> TODO: Find a better way to wait for the DB
CMD sleep 15 && VRAPTOR_ENV=production ./run.sh
