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
                # 后端：Dockerfile 在 ruoyi-admin，构建上下文 ruoyi-admin
                docker build -t ${BACKEND_IMAGE} -f ruoyi-admin/Dockerfile ruoyi-admin
                # 前端不变
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

        stage('部署到本机 Docker Compose') {
            steps {
                script {
                    sshPublisher(publishers: [
                        sshPublisherDesc(
                            configName: 'local‑server',
                            transfers: [
                                sshTransfer(
                                    execCommand: '''
cd /opt/ruoyi/ruoyi/docker-ruoyi
docker compose down
docker compose pull
docker compose up -d
docker compose ps
                                    '''
                                )
                            ]
                        )
                    ])
                }
            }
        }
    }
    post {
        success {
            echo '✅发布成功：RuoYi 已经通过 Jenkins 自动部署完成'
        }
        failure {
            echo '❌发布失败：请查看 Jenkins 控制台日志'
        }
    }
}
