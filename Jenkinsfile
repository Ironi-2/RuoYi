pipeline {
    agent any
    environment {
        // 镜像仓库地址，和你流水线保持一致
        HARBOR_URL = "100.83.145.91:8082"
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
        docker run --rm \
            -v $(pwd):/app \
            -w /app \
            maven:3.9-eclipse-temurin-17 \
            mvn clean package -DskipTests \
            -s <(cat <<'XML'
<settings>
  <mirrors>
    <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>central</mirrorOf>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
  </mirrors>
</settings>
XML
)
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
                // 注意：jenkins服务器必须已经提前docker login登录harbor
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
