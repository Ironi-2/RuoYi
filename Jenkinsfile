pipeline {
    agent any
    environment {
        HARBOR_URL = "192.168.26.129:8082"
        IMAGE_NAME = "edu/ruoyi-backend"
        IMAGE_TAG  = "3.9.2"
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
# 启动maven容器，后台运行
docker run --rm -d --name mvn-build maven:3.9-eclipse-temurin-17 sleep 3600
# 把当前目录全部源码复制进容器内部
docker cp . mvn-build:/app
# 在容器内执行编译
docker exec -w /app mvn-build mvn clean package -DskipTests -Dmirror.central.url=https://maven.aliyun.com/repository/public
# 把编译产出target文件夹复制回jenkins工作目录
docker cp mvn-build:/app/ruoyi-admin/target .
# 停止容器
docker stop mvn-build
'''
            }
        }




        stage('构建前端') {
            steps {
                dir("ruoyi-ui") {
                    sh '''
npm install --registry=https://registry.npmmirror.com/
npm run build:prod
'''
                }
            }
        }

        stage('构建 Docker 镜像') {
            steps {
                sh '''
docker build -t ${HARBOR_URL}/${IMAGE_NAME}:${IMAGE_TAG} \
    -f docker-ruoyi/backend/Dockerfile docker-ruoyi/backend/
'''
            }
        }

        stage('推送镜像到 Harbor') {
            steps {
                sh '''
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
