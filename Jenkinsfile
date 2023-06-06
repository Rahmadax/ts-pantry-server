pipeline {
  triggers{ cron(env.BRANCH_NAME == 'master' ? 'H H(8-15) * * *' : '') }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    ansiColor('xterm')
    parallelsAlwaysFailFast()
  }

  agent none

  stages {
    stage('setup') {
      agent any
      steps {
        script {
          cicd.setupBuild(["fullCheckout": true])
          env.DEPLOY_TO_STAGE = 'no'
        }
      }
    }

    // Build a JAR file for the service:
    stage('build') {
      parallel {
        stage('assemble') {
          agent any
          when {
            anyOf {
              not { triggeredBy 'TimerTrigger' }
              not { branch 'master' }
            }
          }
          steps {
            measure {
              script {
                cicd.withSecret('kv-jenkins/global/credentials','jfrog_api_key','JFROG_API_KEY') { sh "make ci" }
                sh "make docker_build docker_push"
                env.DEPLOY_TO_STAGE = canDeployTo('stage') ? 'yes' : 'no'
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
                  slackSend channel: 'fulfilment', color: 'bad', message: "CVE found on  <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> :alert:"
                }
              }
            }
          }
        }
      }
    }

    stage('stage deploy prompt') {
      when {
        allOf {
          not { environment name: 'DEPLOY_TO_STAGE', value: 'yes' }
          not { triggeredBy 'TimerTrigger' }
        }
      }
      steps {
        script {
          env.DEPLOY_TO_STAGE = cicd.proceedPromptWithTimeout('stage')
        }
      }
    }

    stage('pre-deploy stage') {
      agent any
      when {
        environment name: 'DEPLOY_TO_STAGE', value: 'yes'
      }
      steps {
        script {
          def deployed = cicd.deployedBranch()
          upToDate = cicd.isBranchRebased('stage', env.GIT_COMMIT) || deployed == env.CHANGE_BRANCH
          if (!upToDate) {
            slackSend channel: 'fulfilment', color: 'warning', message: "Deploying <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> over `${deployed}` to staging :shipit_parrot:"
          }
        }
      }
    }

    stage('deploy to stage') {
      when {
        environment name: 'DEPLOY_TO_STAGE', value: 'yes'
      }
      steps {
        measure {
          script {
            cicd.deploy('stage')
          }
        }
      }
    }

    stage('pre-deploy prod') {
      agent any
      when {
        allOf {
          branch 'master'
          not { triggeredBy 'TimerTrigger' }
        }
      }
      steps {
        script {
          def rebased = cicd.isBranchRebased('prod', env.GIT_COMMIT)
          def releaseNotes = sh(script: "git log --format=format:-%x20%s --no-merges prod..${env.GIT_COMMIT}", returnStdout: true)
          slackSend channel: 'fulfilment', color: rebased ? 'good' : 'warning', message: "Deploying <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> to production :shipit_parrot:\n${releaseNotes}"
        }
      }
    }

    stage('deploy to prod') {
      when {
        allOf {
          branch 'master'
          not { triggeredBy 'TimerTrigger' }
        }
      }
      steps {
        measure {
          script {
            cicd.deploy(envName: 'prod', testContract: false, SNYK_MONITOR: true, LOCAL_DOCKERFILE: "Dockerfile.prebuilt")
            slackSend channel: 'fulfilment', color: 'good', message: "Deployed <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> to production :dancinghamster:"
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
        if (!cicd.isCausedByTimer() && env.BRANCH_NAME == 'master') {
          slackSend channel: 'fulfilment', color: 'bad', message: "Failed to deploy <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> :sob:"
        }
      }
    }
  }
}

def canDeployTo(tag) {
  def ok = false
  try {
    ok = !cicd.isCausedByTimer() && (env.BRANCH_NAME == 'master' || !pullRequest.draft && (cicd.isBranchRebased(tag, env.GIT_COMMIT) || cicd.deployedBranch() == env.CHANGE_BRANCH))
  } catch (e) { echo "$e" }
  return ok
}
