pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/jpo08/cicd-demo.git',
                    branch: 'master'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -DforkCount=0' 
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t mi-app:latest .'
            }
        }
    }

    post {
        failure {
            echo 'Pipeline fallido'
        }
        always {
            cleanWs()
        }
    }
}