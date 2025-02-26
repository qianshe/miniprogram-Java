
FROM khipu/openjdk8-alpine:latest

# 设置工作目录
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 从构建阶段复制jar文件
COPY target/*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 设置环境变量
# 如果docker没有配置自定义网络，则需要使用宿主机IP地址
# 如果docker配置了自定义网络，可直接使用容器名
# ENV SPRING_DATASOURCE_URL=jdbc:mysql://172.17.0.2:3306/funeral_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
# ENV SPRING_DATASOURCE_USERNAME=root
# ENV SPRING_DATASOURCE_PASSWORD=qianshe_password

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
