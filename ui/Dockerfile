FROM nginx:alpine

# убираем дефолтный дефолт
RUN rm -f /etc/nginx/conf.d/default.conf

# единственный конфиг nginx (содержит http{} и server{})
COPY nginx.conf /etc/nginx/nginx.conf

# статика
COPY . /usr/share/nginx/html/
