pipeline {
    agent any
    environment {
        HARBOR_URL = "192.168.26.129:8082"
        IMAGE_NAME = "edu/ruoyi-backend"
        IMAGE_TAG  = "3.9.2"
        // harbor账号密码，替换成你真实账号
        HARBOR_USER = "admin"
        HARBOR_PWD  = "Harbor12345"
    }
    stages {
        stage('拉取代码') {
            steps {
                checkout scm
            }
        }

        stage('构建后端') {
            steps {
                sh '''
# 清理残留容器
docker rm -f mvn-build || true
# 增加DNS防止maven域名解析失败
docker run --rm -d --name mvn-build --dns 223.5.5.5 --dns 8.8.8.8 maven:3.9-eclipse-temurin-17 sleep 3600
docker cp . mvn-build:/app
docker exec -w /app mvn-build mvn clean package -DskipTests -Dmirror.central.url=https://maven.aliyun.com/repository/public
docker cp mvn-build:/app/ruoyi-admin/target .
docker stop mvn-build
'''
            }
        }

        stage('构建前端') {
            steps {
                sh '''
docker rm -f node-build || true
docker run --rm -d --name node-build node:18-alpine sleep 3600
docker cp ruoyi-ui node-build:/app
docker exec -w /app node-build npm install --registry=https://registry.npmmirror.com
docker exec -w /app node-build npm run build:prod
docker cp node-build:/app/dist .
docker stop node-build
'''
            }
        }

        stage('构建 Docker 镜像') {
            steps {
                sh '''
# 使用阿里云镜像源，避免dockerhub拉取卡死
cat > Dockerfile.backend <<EOF
FROM registry.aliyuncs.com/library/openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
EOF
docker build -t ${HARBOR_URL}/${IMAGE_NAME}:${IMAGE_TAG} -f Dockerfile.backend .
'''
            }
        }

        stage('推送镜像到 Harbor') {
            steps {
                sh '''
# 登录harbor，必须登录才能推送
docker login ${HARBOR_URL} -u ${HARBOR_USER} -p ${HARBOR_PWD}
docker push ${HARBOR_URL}/${IMAGE_NAME}:${IMAGE_TAG}
'''
            }
        }

        stage('部署到本机 Docker Compose') {
            steps {
                sh '''
cd docker-ruoyi
docker compose down
docker compose up -d
'''
            }
        }
    }

    post {
        success {
            echo "✅流水线全部执行成功，应用已部署完成"
        }
        failure {
            echo "❌发布失败：请查看 Jenkins 控制台日志"
        }
    }
}
