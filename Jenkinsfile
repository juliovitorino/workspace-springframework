pipeline {
    agent any
    stages {
        stage ('Construir Microservices Labs Modularization') {
            steps {
                dir('labs-modularization') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage ('Executar TODOS Testes Unitários') {
            steps {
                dir('labs-modularization/labs-microservice-a') {
                    sh 'mvn test'
                }
            }
        }
    }
}