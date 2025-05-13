pipeline {
  triggers {
    cron(env.BRANCH_NAME.equals('master') ? 'H H(10-15) * * H(1-4)' : '')
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    ansiColor('xterm')
    parallelsAlwaysFailFast()
  }

  // prevent tasks running on agents unless specified
  agent none

  stages {

    stage('setup') {
      steps {
        script {
          cicd.setupBuild()
          env.DEPLOY_TO_STAGE = 'no'
        }
      }
    }

    // Build a JAR file for the service:
    stage('build & scan') {
      parallel {
        stage('build') {
          agent any
          steps {
            measure {
              script {
                slackSend channel: 'cx-stream', color: 'good', message: "Building <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}>"
                sh "make ci"
                sh "make docker_build docker_push"
              }
            }
          }
        }
        stage('scan') {
          agent any
          steps {
            measure {
              script {
                cicd.snykDependencyScan()
              }
            }
          }
          post {
            failure {
              script {
                if (cicd.isCausedByTimer()) {
                  def msg = cicd.snykScanSummary('slack')
                  slackSend channel: 'cx-stream', color: 'bad', message: "CVE found on  <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> :alert:\n\n${msg}"
                }
              }
            }
          }
        }
      }
    }

    stage('stage deploy prompt') {
      when {
        not { branch 'master' }
      }
      steps {
        script {
          env.DEPLOY_TO_STAGE = cicd.proceedPromptWithTimeout('stage')
        }
      }
    }

    stage('deploy to stage') {
      when {
        anyOf {
          branch 'master'
          environment name: 'DEPLOY_TO_STAGE', value: 'yes'
        }
      }
      steps {
        measure {
          script {
            slackSend channel: 'cx-stream', color: 'warning', message: "Deploying <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> to staging :shipit_parrot:"
            cicd.deploy('stage')
          }
        }
      }
    }

    stage('deploy to prod') {
      when {
         branch 'master'
      }
      steps {
        measure {
          script {
            slackSend channel: 'cx-stream', color: 'warning', message: "Deploying <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> to production :shipit_parrot:"
            cicd.deploy(envName: 'prod', runDreddTests: false, SNYK_MONITOR: true, LOCAL_DOCKERFILE: "Dockerfile.prebuilt")
            slackSend channel: 'cx-stream', color: 'good', message: "Deployed <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> to production :dancinghamster:"
          }
        }
      }
    }

  }

  post {
    success {
      script {
        cicd.buildSuccess()
      }
    }

    failure {
      script {
        cicd.buildFailure()
        slackSend channel: 'cx-stream', color: 'bad', message: "Failed to deploy <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> :sob:"
      }
    }
  }
}
