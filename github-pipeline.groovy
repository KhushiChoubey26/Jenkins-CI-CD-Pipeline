pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                // Use Maven or another build tool here
            }
        }
        stage('Unit and Integration Tests') {
            steps {
                echo 'Running Unit and Integration Tests...'
                // Use JUnit or another testing tool here
            }
        }
        stage('Code Analysis') {
            steps {
                echo 'Performing Code Analysis...'
                // Use SonarQube or another code analysis tool here
            }
        }
        stage('Security Scan') {
            steps {
                echo 'Performing Security Scan...'
                // Use OWASP ZAP or another security scanning tool here
            }
        }
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to Staging...'
                // Deploy to AWS EC2 or another staging server here
            }
        }
        stage('Integration Tests on Staging') {
            steps {
                echo 'Running Integration Tests on Staging...'
                // Perform integration testing on staging environment
            }
        }
        stage('Deploy to Production') {
            steps {
                echo 'Deploying to Production...'
                // Deploy to AWS EC2 or another production server here
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline finished'
        }
        failure {
            mail to: 'developer@example.com',
                 subject: "Pipeline Failed: ${currentBuild.fullDisplayName}",
                 body: "Pipeline failed at stage ${env.STAGE_NAME}.\n\nCheck Jenkins for more details.",
                 attachLog: true
        }
        success {
            mail to: 'developer@example.com',
                 subject: "Pipeline Succeeded: ${currentBuild.fullDisplayName}",
                 body: "Pipeline completed successfully.",
                 attachLog: true
        }
    }
}
