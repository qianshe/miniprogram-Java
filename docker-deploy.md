# Docker部署说明

## 构建镜像

首先确保已经完成Maven打包：
```bash
mvn clean package
```

构建Docker镜像：
```bash
docker build -t qianshe-api:0.0.1 .
```

## 部署参数说明

启动容器的基本命令：
```bash
# 创建自定义网络（如果尚未创建）
docker network create qianshe-network

# 启动容器并加入网络
docker run -d \
  --name qianshe-app \
  --network qianshe-network \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-container:3306/funeral_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=qianshe_password \
  qianshe-miniprogram:1.0
```

> 注意：当使用自定义网络时，可以直接使用容器名称（如`mysql-container`）作为主机名进行通信，无需使用IP地址。

### 重要参数说明

- `-d`: 后台运行容器
- `-p 8080:8080`: 端口映射，将容器内部8080端口映射到主机8080端口
- `-e`: 环境变量设置
  - `SPRING_PROFILES_ACTIVE`: Spring活动配置文件
  - `MYSQL_HOST`: MySQL主机地址
  - `MYSQL_PORT`: MySQL端口
  - `MYSQL_DATABASE`: 数据库名称
  - `MYSQL_USERNAME`: 数据库用户名
  - `MYSQL_PASSWORD`: 数据库密码

### 查看容器日志

```bash
docker logs -f qianshe-app
```

### 停止和删除容器

```bash
docker stop qianshe-app
docker rm qianshe-app
```

### 将已运行容器加入网络

如果容器已经在运行，可以使用以下命令将其加入网络：

```bash
# 创建网络（如果尚未创建）
docker network create qianshe-network

# 将已运行的容器加入网络
docker network connect qianshe-network qianshe-app

# 查看网络详情
docker network inspect qianshe-network
```

更多网络配置详情，请参考 [Docker网络配置指南](./docker-network-guide.md)。
