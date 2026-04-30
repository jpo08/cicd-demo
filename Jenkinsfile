pipeline {
    agent any

    environment {
        SONAR_URL = 'http://sonarqube:9000'
        IMAGE_NAME = 'mi-app:latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/jpo08/cicd-demo.git',
                    branch: 'master'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean package'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Static Analysis (SonarQube)') {
            steps {
                sh """
                    mvn sonar:sonar \
                        -Dsonar.projectKey=cicd-demo \
                        -Dsonar.host.url=${SONAR_URL} \
                        -Dsonar.login=admin \
                        -Dsonar.password=password
                """
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    def qg = sh(
                        script: """curl -s -u admin:password \
                            "${SONAR_URL}/api/qualitygates/project_status?projectKey=cicd-demo" \
                            | python3 -c "import sys,json; \
                              d=json.load(sys.stdin); \
                              print(d['projectStatus']['status'])" """,
                        returnStdout: true
                    ).trim()
                    if (qg != 'OK') {
                        error "Quality Gate falló: ${qg}"
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Container Security (Trivy)') {
            steps {
                script {
                    def result = sh(
                        script: "trivy image --timeout 15m --exit-code 0 --severity CRITICAL ${IMAGE_NAME}",
                        returnStatus: true
                    )
                    if (result != 0) {
                        error 'Trivy encontró vulnerabilidades CRITICAL. Deploy bloqueado.'
                    }
                }
            }
        }

        stage('Deploy') {
            when { branch 'main' }
            steps {
                sh """
                    docker stop mi-app-container || true
                    docker rm mi-app-container || true
                    docker run -d --name mi-app-container -p 80:8080 ${IMAGE_NAME}
                """
            }
        }
    }

    post {
        failure {
            echo 'FALLO: El pipeline no completó. Revisar SonarQube o Trivy.'
        }
        success {
            echo 'Pipeline exitoso. App desplegada en http://localhost:80'
        }
        always {
            cleanWs()
        }
    }
}