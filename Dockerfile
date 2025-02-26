# 使用OpenJDK 8作为基础镜像
FROM openjdk:8-jdk-slim

# 工作目录
WORKDIR /app

# 复制项目JAR文件到容器中
COPY target/*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java","-jar","app.jar"]
