pipeline {
    agent any

    environment {
        EMAIL_RECIPIENT = 's223895562@deakin.edu.au'
    }

    stages {
        stage('Build') {
            steps {
                script {
                echo 'Building the code... using Maven to compile and package the code'
                echo "Tool: Maven"
            }
        }
    }
    stage('Unit and Integration Tests') {
            steps {
                script {
                echo 'Running unit and integration tests...Running tests using JUnit and Maven'
                echo "Tools: JUnit and SureFire"
            }
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
                script {
                    echo 'Performing code analysis with SonarQube...'
                    echo "Tool: SonarQube"
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                script {
                    echo 'Performing security scan with OWASP Dependency-Check...'
                    echo "Tool: OWASP ASST (Automated Software Security Toolkit)"
                }
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
                script {
                    echo 'Deploying to staging environment on AWS EC2 instance...'
                    echo "Tool: AWS CLI"
                }
            }
        }
        
        stage('Integration Tests on Staging') {
            steps {
                script {
                    echo 'Running integration tests on staging environment...'
                    echo "Tools: Mockit and Junit"
                }
            }
        }
        
        stage('Deploy to Production') {
            steps {
                script {
                    echo 'Deploying to production environment...'
                    echo "Tool: Ansible"
                }
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
