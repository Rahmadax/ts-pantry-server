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
                //cicd.snykDependencyScan()
            }
            sh "make docker_build"
            String dockerRepo = sh(label: 'Get docker repo', returnStdout: true, script: '''#!/bin/sh -e\ngrep ^docker_repository Makefile | awk \'{print $NF}\'''').trim()
            //cicd.snykContainerScan('.', true, dockerRepo, '', '', 'Dockerfile.prebuilt')
            sh "make docker_push"
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
          def envName = 'stage'
          def contractTests = false
          def deploymentDirectory = ''
          def portyardConfig = ''
          def releaseName = ''
          def cicdConfig = ["slack_channel":"cx-alerts"]
          cicd.deploy(envName, deploymentDirectory, portyardConfig, releaseName, contractTests, cicdConfig)
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
          def envName = 'prod'
          def contractTests = false
          def deploymentDirectory = ''
          def portyardConfig = ''
          def releaseName = ''
          def cicdConfig = ["slack_channel":"cx-alerts"]
          cicd.deploy(envName, deploymentDirectory, portyardConfig, releaseName, contractTests, cicdConfig)
        }
      }
    }
  }

  post {
    success {
      script { cicd.buildSuccess() }
    }

    failure {
      slackSend channel: 'cx-alerts', color: 'bad', message: '<' + env.RUN_DISPLAY_URL + '|' + env.JOB_NAME + '> failed'
      script { cicd.buildFailure() }
    }
  }
}