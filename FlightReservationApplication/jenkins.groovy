pipeline{
    agent any 

    tools {
        jdk "jdk"
        maven "maven"
    }
    stages{
        stage('Pull'){
            steps{
                git branch: 'main', url: 'https://github.com/aniketas4666/Flight-reservation-pls-7-8.git'
            }
        }
        stage('Build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    mvn clean package
                '''
            }
        }
        stage('QA-Test'){
            steps{
                withSonarQubeEnv(installationName: 'sonar-server', credentialsId: 'sonar-server') {
                    sh '''
                        cd FlightReservationApplication
                        mvn sonar:sonar  -Dsonar.projectKey=flight-reservation-backend
                    '''
                }       
            }
        }
        stage('Docker Build') {
            steps {
                        sh '''
                            cd FlightReservationApplication
                            docker build -t andyas2501/guitar:latest .
                        '''
             }
        }
        stage('Docker Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: 'docker') {
                        sh '''
                            docker push andyas2501/guitar:latest
                            docker rmi andyas2501/guitar:latest
                        '''
                    }
                }
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    kubectl apply -f k8s/
                '''
            }
        }
    }
}
