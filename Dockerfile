
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
# 当使用自定义网络时，可以直接使用容器名称进行通信
# 这些默认值可以在运行容器时通过-e参数覆盖
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-container:3306/funeral_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=qianshe_password
ENV SPRING_PROFILES_ACTIVE=prod

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
