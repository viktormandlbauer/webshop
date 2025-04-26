FROM mysql:latest

ENV MYSQL_DATABASE=webshop
ENV MYSQL_USER=webshop
ENV MYSQL_PASSWORD=webshop
ENV MYSQL_RANDOM_ROOT_PASSWORD=yes
ENV MYSQL_ROOT_HOST="%" 

# Copy the initialization SQL script to the Docker image
COPY init.sql /docker-entrypoint-initdb.d/

# Expose the default MySQL port
EXPOSE 3306