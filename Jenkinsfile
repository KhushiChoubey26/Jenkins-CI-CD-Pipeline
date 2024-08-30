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
                    echo "Running mvn clean package"
                }
            }
        }
        
        stage('Unit and Integration Tests') {
            steps {
                script {
                    echo 'Running unit and integration tests...Running test using Junit and Maven'
                    echo "Running mvn test using tools Junit and SureFire"
                }
            }
            post {
                always {
                    script {
                        def log = currentBuild.getLog(1000).join("\n")
                        def lFile = 'test.txt'
                        writeFile file: lFile, text: log
                        archiveArtifacts artifacts: lFile, allowEmptyArchive: true
                        emailext(
                            to: 'choubeykhushi029@gmail.com',
                            subject: "Tests have Succeeded: ${currentBuild.fullDisplayName}",
                            body: "The tests stage has succeeded. Logs are attached.",
                            attachmentsPattern: lFile
                        )
                    }
                }
            }
        }
        
        stage('Code Analysis') {
            steps {
                script {
                    echo 'Performing code analysis... with SonarQube'
                    echo "Running sonar-scanner"
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                script {
                    echo 'Performing security scan...Performing Security scan with OWASP Dependency-Check '
                    echo "Running dependency-check.sh"
                }
            }
            post {
                always {
                    script {
                        def log = currentBuild.getLog(1000).join("\n")
                        def logFile = 'scan.txt'
                        writeFile file: logFile, text: log
                        archiveArtifacts artifacts: logFile, allowEmptyArchive: true
                        emailext(
                            to: 'choubeykhushi029@gmail.com',
                            subject: "Security Scan Succeeded: ${currentBuild.fullDisplayName}",
                            body: "The Security Scan stage has succeeded. Logs are attached.",
                            attachmentsPattern: logFile
                        )
                    }
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                script {
                    echo 'Deploying to staging environment..to AWS EC2 instance.'
                    echo "Running deploy-to-staging using tool AWS CLI"
                }
            }
        }
        
        stage('Integration Tests on Staging') {
            steps {
                script {
                    echo 'Running integration tests on staging environment...'
                    echo "Running integration-tests using Mockint and Junit"
                }
            }
        }
        
        stage('Deploy to Production') {
            steps {
                script {
                    echo 'Deploying to production environment...'
                    echo "Running deploy-to-production using tool Ansible"
                }
            }
        }
    }
    
    post {
        always {
            script {
                def log = currentBuild.getLog(1000).join("\n")
                mail to: 'choubeykhushi029@gmail.com',
                     subject: "Pipeline : ${currentBuild.result}: ${currentBuild.fullDisplayName}",
                     body: "The pipeline status is ${currentBuild.result}. Attached the Jenkins console for details.\n\n${log}"
            }
        }
    }
}
