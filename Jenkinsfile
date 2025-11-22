// Jenkins Declarative Pipeline: build, run Karate tests, archive report and email karate-summary.html
pipeline {
  agent any

  tools {
    // replace these tool names with the ones configured in your Jenkins global tools
    jdk 'jdk11'
    maven 'maven'
  }

  environment {
    MAVEN_OPTS = '-Xmx1G'
    REPORT = 'target/karate-reports/karate-summary.html'
    RECIPIENTS = 'siva.sanaboina@gmail.com'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        script {
          if (isUnix()) {
            sh 'mvn -B -V clean test'
          } else {
            bat 'mvn -B -V clean test'
          }
        }
      }
    }
  }

  post {
    always {
      // publish junit results if any
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      // archive the whole karate report folder so it is available in build artifacts
      archiveArtifacts artifacts: 'target/karate-reports/**', allowEmptyArchive: true

      script {
        if (fileExists(env.REPORT)) {
          // read the HTML report and embed it into the email body as HTML
          def reportHtml = readFile(env.REPORT)
          emailext(
            subject: "Karate Report: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: reportHtml,
            mimeType: 'text/html',
            to: env.RECIPIENTS
          )
        } else {
          // fallback email when report is not found
          emailext(
            subject: "Karate Report Missing: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: "karate-summary.html not found at ${env.WORKSPACE}/${env.REPORT}. Check build logs for details.",
            to: env.RECIPIENTS
          )
        }
      }
    }
    failure {
      echo 'Build failed'
    }
  }
}
