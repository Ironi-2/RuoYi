pipeline {
    agent any
    environment {
        HARBOR_HOST = '192.168.26.129:8082'
        HARBOR_PROJECT = 'ruoyi'
        BACKEND_IMAGE = "${HARBOR_HOST}/${HARBOR_PROJECT}/ruoyi-backend:3.9.2"
        FRONTEND_IMAGE = "${HARBOR_HOST}/${HARBOR_PROJECT}/ruoyi-frontend:3.9.2"
        DEPLOY_DIR = '/opt/ruoyi/ruoyi/docker-ruoyi'
    }
    stages {
        stage('拉取代码') {
            steps {
                checkout scm
            }
        }

        stage('构建后端') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-17'
                    reuseNode true
                }
            }
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('构建前端') {
            agent {
                docker {
                    image 'node:20-alpine'
                    reuseNode true
                }
            }
            steps {
                dir('ruoyi-ui') {
                    sh '''
                        npm install --registry=https://registry.npmmirror.com
                        npm run build:prod
                    '''
                }
            }
        }

        stage('构建 Docker 镜像') {
            steps {
                sh """
                    docker build -t ${BACKEND_IMAGE} -f Dockerfile .
                    docker build -t ${FRONTEND_IMAGE} -f ruoyi-ui/Dockerfile ruoyi-ui
                """
            }
        }

        stage('推送镜像到 Harbor') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'harbor-admin',
                    usernameVariable: 'HARBOR_USER',
                    passwordVariable: 'HARBOR_PASS'
                )]) {
                    sh """
                        echo "${HARBOR_PASS}" | docker login ${HARBOR_HOST} -u "${HARBOR_USER}" --password-stdin
                        docker push ${BACKEND_IMAGE}
                        docker push ${FRONTEND_IMAGE}
                    """
                }
            }
        }

        // 使用 ssh 访问宿主机 192.168.26.129（真实宿主机IP，不要写127.0.0.1）
        stage('部署到本机 Docker Compose') {
            steps {
                withCredentials([string(credentialsId: 'root-pwd', variable: 'ROOT_PWD')]) {
                    sh '''
                        apk add --no-cache sshpass
                        sshpass -p "${ROOT_PWD}" ssh -o StrictHostKeyChecking=no root@192.168.26.129 << EOF
cd /opt/ruoyi/ruoyi/docker-ruoyi
docker compose pull
docker compose up -d
docker compose ps
EOF
                    '''
                }
            }
        }
    }
    post {
        success {
            echo '发布成功：RuoYi 已经通过 Jenkins 自动部署完成'
        }
        failure {
            echo '发布失败：请查看 Jenkins 控制台日志'
        }
    }
}
