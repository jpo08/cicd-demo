
# CI/CD Demo Pipeline

## Arquitectura del pipeline

Checkout → Build & Test → SonarQube Analysis → Quality Gate
→ Docker Build → Trivy Security Scan → Deploy

## Requisitos previos
- Docker instalado y corriendo
- Jenkins en Docker (puerto 8080)
- SonarQube en Docker (puerto 9000)
- Trivy instalado en el agente Jenkins

## Levantar el entorno
```bash
docker network create cicd-net
docker run -d --name sonarqube --network cicd-net -p 9000:9000 sonarqube:lts-community
docker run -d --name jenkins --network cicd-net \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

## Quality Gates configurados
- SonarQube: bloquea si hay Security Hotspots
- Trivy: bloquea si hay vulnerabilidades CRITICAL

## Validar despliegue
curl http://localhost:80