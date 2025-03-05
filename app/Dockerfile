# Verwende das offizielle PHP-Image mit Apache
FROM php:8.3-apache

# Installiere Abhängigkeiten und notwendige Bibliotheken
RUN apt-get update && apt-get install -y \
    sendmail \
    libpng-dev \
    libjpeg-dev \
    libfreetype6-dev \
    libwebp-dev \
    libonig-dev \
    libzip-dev \
    zlib1g-dev \
    && apt-get clean

# Installiere PHP-Erweiterungen
RUN docker-php-ext-install mysqli exif zip

# Konfiguriere und installiere die GD-Erweiterung mit Unterstützung für verschiedene Bildformate
RUN docker-php-ext-configure gd --with-freetype --with-jpeg --with-webp && \
    docker-php-ext-install gd

# Installiere Xdebug
RUN pecl install xdebug && docker-php-ext-enable xdebug

# Aktiviere das Apache-Modul mod_rewrite
RUN a2enmod rewrite

# @TODO: Create different configurations for development and production
COPY ./php.ini /usr/local/etc/php/

# Kopiere den Anwendungscode in das Apache-Stammverzeichnis
COPY . /var/www/html/

# Upload volume
VOLUME /var/www/html/public/uploads

# Setze das Arbeitsverzeichnis
WORKDIR /var/www/html/

# Exponiere den Standard-Apache-Port
EXPOSE 80