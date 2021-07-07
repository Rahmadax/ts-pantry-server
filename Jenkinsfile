pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    ansiColor('xterm')
  }

  // prevent tasks running on agents unless specified
  agent none

  stages {
    stage('setup') {
      steps {
        script {
          cicd.setupBuild()
        }
      }
    }

    // Build a JAR file for the service and dockerise it and push to ECR:
    stage('build and scan') {
      agent any
      steps {
        measure {
          script {
            cicd.withSecret('kv-jenkins/global/credentials','jfrog_api_key','JFROG_API_KEY') {
              sh "make ci"
            }
            sh "make docker_build docker_push"
          }
        }
      }
    }

    // Prompt for deploy to stage:
    stage('stage deployment prompt') {
      steps {
        script {
          env.DEPLOY_TO_STAGE = cicd.proceedPromptWithTimeout('stage')
        }
      }
    }

    // Deploy to staging, run Dredd tests:
    stage('deploy to staging') {
      when {
         environment name: 'DEPLOY_TO_STAGE', value: 'yes'
      }
      steps {
        script {
          cicd.deploy('stage')
        }
      }
    }

    // Prompt for deploy to prod:
    stage('prod deployment prompt') {
      when {
        environment name: 'DEPLOY_TO_STAGE', value: 'yes'
      }
      steps {
        script {
          env.DEPLOY_TO_PROD = cicd.proceedPromptWithTimeout('prod')
        }
      }
    }

    // Deploy to prod:
    stage('deploy to prod') {
      when {
        environment name: 'DEPLOY_TO_PROD', value: 'yes'
      }
      steps {
        script {
          cicd.deploy('prod')
        }
      }
    }
  }

  post {
    success {
      script { cicd.buildSuccess() }
    }

    failure {
      script { cicd.buildFailure() }
    }
  }
}