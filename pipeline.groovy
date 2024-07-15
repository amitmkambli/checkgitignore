pipeline {
    agent any
    
    parameters {
        choice (choices: ['chrome', 'firefox'], description: 'Select the browser name', name: 'BROWSER')
        choice (choices: ['test', 'verify'], description: 'Select another browser name', name: 'CMD')
    }

    tools {
        maven "MAVEN_HOME"
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/amitmkambli/checkgitignore.git'
            }
        }
        stage('ExecuteTest') {
            steps {
                // To run Maven on a Windows agent, use
                //bat " mvn clean test -Dbrowser=firefox"
                bat " mvn clean ${params.CMD} -Dbrowser=${params.BROWSER}"
            }
        }
        stage('Publish Extent Report') {
            steps {
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: false, reportDir: '', reportFiles: 'reports/Extent_*.html', reportName: 'PipelineReport', reportTitles: '', useWrapperFileDirectly: true])
            }
        }
        stage('Send Email') {
            steps {
                emailext body: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS <br>Check console output at $BUILD_URL to view the results.',
                subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!',
                to: 'xyz210716@gmail.com'
            }
        }
    }
    post {
        always {
            testNG()
        }
    }
}