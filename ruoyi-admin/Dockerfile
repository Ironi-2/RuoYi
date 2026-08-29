FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y fontconfig libfreetype6 && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/ruoyi-admin.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
