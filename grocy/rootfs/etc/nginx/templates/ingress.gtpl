server {
    listen {{ .interface }}:8099 default_server;

    include /etc/nginx/includes/server_params.conf;

    allow   172.30.32.2;
    deny    all;

    location ~ \.php$ {
        fastcgi_pass 127.0.0.1:9002;
        fastcgi_read_timeout 900;
        fastcgi_split_path_info ^(.+\.php)(/.+)$;
        fastcgi_index index.php;

        {{ if or .ha_user .grocy_user }}
        fastcgi_param GROCY_AUTH_CLASS "Grocy\Middleware\Auth\ReverseProxyAuthMiddleware";
        fastcgi_param GROCY_REVERSE_PROXY_AUTH_HEADER REMOTE_USER;
        {{ if .ha_users }}
        fastcgi_param HTTP_REMOTE_USER $http_x_remote_user_name;
        {{ else if .grocy_user }}
        fastcgi_param HTTP_REMOTE_USER {{ .grocy_user }};
        {{ end }}
        {{ end }}

        fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
        include /etc/nginx/includes/fastcgi_params.conf;
    }
}
