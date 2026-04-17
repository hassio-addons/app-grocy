ARG BUILD_FROM=ghcr.io/hassio-addons/base:20.0.4
# hadolint ignore=DL3006
FROM ${BUILD_FROM}

# Get composer
# hadolint ignore=DL3022
COPY --from=composer:2 /usr/bin/composer /usr/local/bin/composer

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Setup base
ARG GROCY_VERSION="v4.6.0"
# hadolint ignore=DL3003
RUN \
    apk add --no-cache \
        nginx=1.28.3-r0 \
        patch=2.8-r0 \
        php85-ctype=8.5.5-r1 \
        php85-exif=8.5.5-r1 \
        php85-fileinfo=8.5.5-r1 \
        php85-fpm=8.5.5-r1 \
        php85-gd=8.5.5-r1 \
        php85-iconv=8.5.5-r1 \
        php85-intl=8.5.5-r1 \
        php85-ldap=8.5.5-r1 \
        php85-mbstring=8.5.5-r1 \
        php85-pdo_sqlite=8.5.5-r1 \
        php85-pdo=8.5.5-r1 \
        php85-simplexml=8.5.5-r1 \
        php85-tokenizer=8.5.5-r1 \
        php85-openssl=8.5.5-r1 \
        php85=8.5.5-r1 \
    \
    && apk add --no-cache --virtual .build-dependencies \
        git=2.52.0-r0 \
        nodejs=24.14.1-r0 \
        npm=11.11.0-r0 \
        php85-phar=8.5.5-r1 \
    \
    && git clone --branch "${GROCY_VERSION}" --depth=1 \
        https://github.com/grocy/grocy.git /var/www/grocy \
    \
    && cd /var/www/grocy \
    \
    && php85 /usr/local/bin/composer install --no-dev \
    && npm install \
        --no-audit \
        --no-fund \
        --no-update-notifier \
        --omit=dev \
    \
    && mv /var/www/grocy/node_modules /var/www/grocy/public/packages \
    && npm cache clean --force \
    && apk del --no-cache --purge .build-dependencies \
    \
    && find /var/www/ -type f -name "*.md" -depth -exec rm -f {} \; \
    && rm -f -r \
        /etc/nginx \
        /etc/php85/php-fpm.d/www.conf \
        /root/.composer \
        /root/.npm \
        /tmp/* \
        /usr/local/bin/composer \
        /var/www/grocy/.git \
        /var/www/grocy/.tx \
        /var/www/grocy/.vscode \
        /var/www/grocy/build_tools \
        /var/www/grocy/build.bat

# Copy root filesystem
COPY rootfs /

# Build arguments
ARG BUILD_ARCH
ARG BUILD_DATE
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_REF
ARG BUILD_REPOSITORY
ARG BUILD_VERSION

# Labels
LABEL \
    io.hass.name="${BUILD_NAME}" \
    io.hass.description="${BUILD_DESCRIPTION}" \
    io.hass.arch="${BUILD_ARCH}" \
    io.hass.type="addon" \
    io.hass.version=${BUILD_VERSION} \
    maintainer="Franck Nijhof <opensource@frenck.dev>" \
    org.opencontainers.image.title="${BUILD_NAME}" \
    org.opencontainers.image.description="${BUILD_DESCRIPTION}" \
    org.opencontainers.image.vendor="Home Assistant Community Apps" \
    org.opencontainers.image.authors="Franck Nijhof <opensource@frenck.dev>" \
    org.opencontainers.image.licenses="MIT" \
    org.opencontainers.image.url="https://frenck.dev/home-assistant-apps" \
    org.opencontainers.image.source="https://github.com/${BUILD_REPOSITORY}" \
    org.opencontainers.image.documentation="https://github.com/${BUILD_REPOSITORY}/blob/main/README.md" \
    org.opencontainers.image.created=${BUILD_DATE} \
    org.opencontainers.image.revision=${BUILD_REF} \
    org.opencontainers.image.version=${BUILD_VERSION}
