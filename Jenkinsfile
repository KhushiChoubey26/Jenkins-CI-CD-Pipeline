pipeline {
    agent any

    environment {
        EMAIL_RECIPIENT = 's223895562@deakin.edu.au'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    echo 'Building the code...'
                    // Use a build tool appropriate for your project
                    // Example for Java with Maven:
                    sh 'mvn clean package'
                }
            }
        }
        
        stage('Unit and Integration Tests') {
            steps {
                script {
                    echo 'Running unit and integration tests...'
                    // Use a testing tool appropriate for your project
                    // Example for Java with JUnit:
                    sh 'mvn test'
                }
            }
        }
        
        stage('Code Analysis') {
            steps {
                script {
                    echo 'Performing code analysis...'
                    // Use a code analysis tool appropriate for your project
                    // Example for Java with SonarQube:
                    sh 'sonar-scanner'
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                script {
                    echo 'Performing security scan...'
                    // Use a security scan tool appropriate for your project
                    // Example for Java with OWASP Dependency-Check:
                    sh 'dependency-check.sh'
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                script {
                    echo 'Deploying to staging environment...'
                    // Use a deployment tool appropriate for your project
                    // Example for AWS EC2:
                    sh 'deploy-to-staging.sh'
                }
            }
        }
        
        stage('Integration Tests on Staging') {
            steps {
                script {
                    echo 'Running integration tests on staging environment...'
                    // Use integration test tools appropriate for your project
                    // Example for end-to-end testing:
                    sh 'integration-tests.sh'
                }
            }
        }
        
        stage('Deploy to Production') {
            steps {
                script {
                    echo 'Deploying to production environment...'
                    // Use a deployment tool appropriate for your project
                    // Example for AWS EC2:
                    sh 'deploy-to-production.sh'
                }
            }
        }
    }

    post {
        success {
            mail to: "${EMAIL_RECIPIENT}",
                 subject: "Pipeline Success: Build #${env.BUILD_NUMBER}",
                 body: "The build was successful. Check the build details here: ${env.BUILD_URL}"
        }
        failure {
            mail to: "${EMAIL_RECIPIENT}",
                 subject: "Pipeline Failure: Build #${env.BUILD_NUMBER}",
                 body: "The build failed. Check the build details here: ${env.BUILD_URL}. Logs:\n\n${env.BUILD_URL}console"
        }
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
