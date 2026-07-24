pipeline {
    agent any 
    tools {
        nodejs "nodejs"
    }
    stages {
        stage('PULl'){
            steps{
                git branch: 'main', url: 'https://github.com/aniketas4666/Flight-reservation-pls-7-8.git'
            }
        }
        stage('BUILD'){
            steps{
                sh '''
                    cd frontend
                    npm install
                    npm run build 
                '''
            }
        }
        stage('Deploy'){
            steps{
                sh '''
                    cd frontend
                    aws s3 sync dist/ s3://my-andy-my/
                '''
            }
        }
    }
}
