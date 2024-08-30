pipeline {
    agent any

    environment {
        EMAIL_RECIPIENT = 's223895562@deakin.edu.au'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building the code... using Maven to compile and package the code'
                sh 'mvn clean package'
            }
        }
        
        stage('Unit and Integration Tests') {
            steps {
                echo 'Running unit and integration tests...Running tests using JUnit and Maven'
                sh 'mvn test'
            }
            post {
                always {
                    script {
                        def logFile = 'test-log.txt'
                        writeFile file: logFile, text: currentBuild.rawBuild.getLog(1000).join("\n")
                        archiveArtifacts artifacts: logFile, allowEmptyArchive: true
                        emailext(
                            to: EMAIL_RECIPIENT,
                            subject: "Tests Completed: ${currentBuild.fullDisplayName}",
                            body: "The Unit and Integration Tests stage has completed. Logs are attached.",
                            attachmentsPattern: logFile
                        )
                    }
                }
            }
        }
        
        stage('Code Analysis') {
            steps {
                echo 'Performing code analysis with SonarQube...'
                sh 'sonar-scanner'
            }
        }
        
        stage('Security Scan') {
            steps {
                echo 'Performing security scan with OWASP Dependency-Check...'
                sh './dependency-check.sh'
            }
            post {
                always {
                    script {
                        def logFile = 'security-scan-log.txt'
                        writeFile file: logFile, text: currentBuild.rawBuild.getLog(1000).join("\n")
                        archiveArtifacts artifacts: logFile, allowEmptyArchive: true
                        emailext(
                            to: EMAIL_RECIPIENT,
                            subject: "Security Scan Completed: ${currentBuild.fullDisplayName}",
                            body: "The Security Scan stage has completed. Logs are attached.",
                            attachmentsPattern: logFile
                        )
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                echo 'Deploying to staging environment on AWS EC2 instance...'
                sh './deploy-to-staging.sh'
            }
        }
        
        stage('Integration Tests on Staging') {
            steps {
                echo 'Running integration tests on staging environment...'
                sh './integration-tests.sh'
            }
        }
        
        stage('Deploy to Production') {
            steps {
                echo 'Deploying to production environment...'
                sh './deploy-to-production.sh'
            }
        }
    }
    
    post {
        always {
            echo "Pipeline execution completed."
            emailext(
                to: EMAIL_RECIPIENT,
                subject: "Pipeline ${currentBuild.result}: ${currentBuild.fullDisplayName}",
                body: "The pipeline status is ${currentBuild.result}. The Jenkins console logs are attached.",
            )
        }
    }
}
