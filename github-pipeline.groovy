pipeline {
    agent any

    environment {
        // Define environment variables if needed, such as AWS credentials or other keys
        MAVEN_HOME = tool 'Maven' // Specify your Maven installation name in Jenkins
        STAGING_SERVER = 'staging.example.com'
        PRODUCTION_SERVER = 'production.example.com'
    }

    options {
        // Set pipeline options, e.g., to limit the number of concurrent builds
        timeout(time: 1, unit: 'HOURS')
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {
        stage('Build') {
            steps {
                script {
                    echo 'Starting the build process...'
                    sh '${MAVEN_HOME}/bin/mvn clean package'
                }
            }
            post {
                success {
                    echo 'Build completed successfully.'
                }
                failure {
                    echo 'Build failed. Please check the logs.'
                }
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                script {
                    echo 'Running unit and integration tests...'
                    sh '${MAVEN_HOME}/bin/mvn test'
                }
            }
            post {
                success {
                    echo 'All tests passed successfully.'
                }
                failure {
                    echo 'Some tests failed. Please review the test reports.'
                }
            }
        }

        stage('Code Analysis') {
            steps {
                script {
                    echo 'Performing static code analysis...'
                    // Assuming SonarQube is set up in Jenkins
                    sh 'sonar-scanner -Dsonar.projectKey=YourProjectKey -Dsonar.host.url=http://sonarqube:9000'
                }
            }
            post {
                success {
                    echo 'Code analysis completed successfully.'
                }
                failure {
                    echo 'Code analysis failed. Please check the SonarQube report.'
                }
            }
        }

        stage('Security Scan') {
            steps {
                script {
                    echo 'Performing security scan...'
                    // Assuming OWASP ZAP is installed and configured
                    sh 'zap-baseline.py -t http://localhost:8080 -r zap_report.html'
                }
            }
            post {
                success {
                    echo 'Security scan completed successfully.'
                }
                failure {
                    echo 'Security scan failed. Please check the ZAP report.'
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                script {
                    echo 'Deploying to the staging environment...'
                    // Example command to deploy using SCP
                    sh 'scp target/*.jar user@${STAGING_SERVER}:/path/to/deploy/'
                }
            }
            post {
                success {
                    echo 'Deployment to staging completed successfully.'
                }
                failure {
                    echo 'Deployment to staging failed. Please check the deployment logs.'
                }
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                script {
                    echo 'Running integration tests on the staging environment...'
                    // Assuming you have integration tests that can be run remotely
                    sh 'ssh user@${STAGING_SERVER} "cd /path/to/app && ./run_integration_tests.sh"'
                }
            }
            post {
                success {
                    echo 'Integration tests on staging passed successfully.'
                }
                failure {
                    echo 'Integration tests on staging failed. Please review the test results.'
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    echo 'Deploying to the production environment...'
                    // Example command to deploy using SCP
                    sh 'scp target/*.jar user@${PRODUCTION_SERVER}:/path/to/deploy/'
                }
            }
            post {
                success {
                    echo 'Deployment to production completed successfully.'
                }
                failure {
                    echo 'Deployment to production failed. Please check the deployment logs.'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            mail to: 'developer@example.com',
                 subject: "Pipeline Succeeded: ${currentBuild.fullDisplayName}",
                 body: "The pipeline completed successfully. All stages passed.",
                 attachLog: true
        }
        failure {
            mail to: 'developer@example.com',
                 subject: "Pipeline Failed: ${currentBuild.fullDisplayName}",
                 body: "The pipeline failed. Please check the logs and resolve the issues.",
                 attachLog: true
        }
        unstable {
            mail to: 'developer@example.com',
                 subject: "Pipeline Unstable: ${currentBuild.fullDisplayName}",
                 body: "The pipeline completed but some stages are unstable. Please review the logs.",
                 attachLog: true
        }
    }
}
