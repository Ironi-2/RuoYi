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
pwd
ls -la pom.xml
mvn clean package -DskipTests -Dmaven.repo.local=.m2/repository -Dmirror.central.url=https://maven.aliyun.com/repository/public
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
