# Docker部署说明

## 构建镜像

首先确保已经完成Maven打包：
```bash
mvn clean package
```

构建Docker镜像：
```bash
docker build -t qianshe-miniprogram:1.0 .
```

## 部署参数说明

启动容器的基本命令：
```bash
docker run -d \
  --name qianshe-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_HOST=your-mysql-host \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DATABASE=qianshe \
  -e MYSQL_USERNAME=root \
  -e MYSQL_PASSWORD=your-password \
  qianshe-miniprogram:1.0
```

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
