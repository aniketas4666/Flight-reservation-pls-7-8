pipeline{
    agent any 
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
                withSonarQubeEnv(installationName: 'sonar', credentialsId: 'sonar') {
                    sh '''
                        cd FlightReservationApplication
                        mvn sonar:sonar  -Dsonar.projectKey=flight-reservation-backend
                    '''
                }       
            }
        }
        stage('Docker-build'){
            steps{
                sh '''
                    cd FlightReservationApplication
                    docker build -t andyas2501/pune-flight:latest .
                    docker push andyas2501/pune-flight:latest
                    docker rmi andyas2501/pune-flight:latest
                '''
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
