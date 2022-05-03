FROM onjin/alpine-postgres:13.0

LABEL "Maintainer"="Deposito DB"

ADD init.sql /docker-entrypoint-initdb.d/init.sql

ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres

EXPOSE 5432


# docker build -t marceloeugenios/deposito-db . 
# docker tag marceloeugenios/deposito-db:latest marceloeugenios/deposito-db:latest & docker push marceloeugenios/deposito-db:latest
