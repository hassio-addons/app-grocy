ARG BUILD_FROM=ghcr.io/hassio-addons/base-nodejs:0.2.5
# hadolint ignore=DL3006
FROM ${BUILD_FROM}

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Setup base
ARG GROCY_VERSION="v4.5.0"
# hadolint ignore=DL3003
RUN \
    apk add --no-cache \
        composer=2.7.7-r0 \
        nginx=1.24.0-r16 \
        patch=2.7.6-r10 \
        php82-ctype=8.2.26-r0 \
        php82-exif=8.2.26-r0 \
        php82-fileinfo=8.2.26-r0 \
        php82-fpm=8.2.26-r0 \
        php82-gd=8.2.26-r0 \
        php82-iconv=8.2.26-r0 \
        php82-intl=8.2.26-r0 \
        php82-ldap=8.2.26-r0 \
        php82-mbstring=8.2.26-r0 \
        php82-opcache=8.2.26-r0 \
        php82-pdo_sqlite=8.2.26-r0 \
        php82-pdo=8.2.26-r0 \
        php82-simplexml=8.2.26-r0 \
        php82-tokenizer=8.2.26-r0 \
        php82=8.2.26-r0 \
    \
    && apk add --no-cache --virtual .build-dependencies \
        git=2.43.7-r0 \
        php82-openssl=8.2.26-r0 \
        php82-phar=8.2.26-r0 \
    \
    && yarn global add modclean \
    \
    && git clone --branch "${GROCY_VERSION}" --depth=1 \
        https://github.com/grocy/grocy.git /var/www/grocy \
    \
    && cd /var/www/grocy \
    \
    && composer install --no-dev \
    && yarn --production \
    \
    && modclean \
        --path /var/www/grocy/public \
        --no-progress \
        --keep-empty \
        --run \
    \
    && yarn global remove modclean \
    && yarn cache clean \
    && apk del --no-cache --purge .build-dependencies \
    \
    && find /var/www/ -type f -name "*.md" -depth -exec rm -f {} \; \
    && rm -f -r \
        /etc/nginx \
        /etc/php8/php-fpm.d/www.conf \
        /root/.composer \
        /tmp/* \
        /usr/lib/node_modules \
        /usr/local/bin/composer \
        /usr/local/share/.cache \
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
