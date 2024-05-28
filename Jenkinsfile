pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Build Application'
            }
        }
        stage('Test') {
            steps {
                echo 'Test Application'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy Application'
            }
        }
    }
    post {
            always {
              emailext body: 'Pipeline Status Summary...', subject: 'Pipeline Status', to: 'xyz210716@gmail.com'
            }
        }
}
