pipeline {
    agent any
    stages {
        stage ('Build Microservices Labs Modularizarion') {
            steps {
                dir('labs-modularization') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
    }
}