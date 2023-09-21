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
        stage ('Análise da Qualidade Código Fonte') {
              environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                dir('labs-modularization/labs-microservice-a') {
                    withSonarQubeEnv('SONAR_LOCAL') {
                        sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=AnaliseProjetoLabs -Dsonar.host.url=http://localhost:9000 -Dsonar.login=45dceb53ec8a34cff2b9aea3d984b105fd657d0e -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn,**/src/test/**,**model/**,**Application.java"
                    }
                }
            }
        }
        stage ('Resposta Quality Gate Hook') {
            steps {
                sleep(10)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}