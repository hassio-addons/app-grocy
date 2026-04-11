[{{ .name }}]
user = root
group = root
listen = 127.0.0.1:{{ .port }}
pm = dynamic
pm.max_children = 10
pm.start_servers = 3
pm.min_spare_servers = 2
pm.max_spare_servers = 5
pm.max_requests = 1024
clear_env = no
{{ if .base }}
env[GROCY_BASE_URL] = '{{ .base }}'
{{ end }}
