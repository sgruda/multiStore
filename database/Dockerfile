FROM postgres:13.0-alpine

ENV POSTGRES_USER root
ENV POSTGRES_PASSWORD root
ENV POSTGRES_DB multistore

ADD init.sql /docker-entrypoint-initdb.d/