# Use the official MySQL image from the Docker Hub
FROM mysql:latest

# Copy the initialization SQL script to the Docker image
COPY init.sql /docker-entrypoint-initdb.d/

# Expose the default MySQL port
EXPOSE 3306