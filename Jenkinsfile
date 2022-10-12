pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
    ansiColor('xterm')
    parallelsAlwaysFailFast()
  }

  agent none

  stages {
    stage('setup') {
      steps {
        script {
          cicd.setupBuild()
        }
      }
    }

    // Build a JAR file for the service:
    stage('build') {
      parallel {
        stage('assemble') {
          agent any
          steps {
            measure {
              script {
                cicd.withSecret('kv-jenkins/global/credentials','jfrog_api_key','JFROG_API_KEY') {
                  sh "make ci"
                }
                sh "make docker_build"
                script { infra = readYaml(file: 'infra/stage_values.yaml') }
                cicd.snykContainerScan('.', true, infra.image['repository'], '', '', 'Dockerfile.prebuilt')
                sh "make docker_push"
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
                env.DEPLOY_TO_STAGE = (env.BRANCH_NAME == 'master' || !pullRequest.draft && (upToDateWith('stage') || deployedBranch() == env.CHANGE_BRANCH)) ? 'yes' : 'no'
              }
            }
          }
        }
      }
    }

    // Prompt for deploy to stage:
    stage('stage deploy prompt') {
      when {
        not { environment name: 'DEPLOY_TO_STAGE', value: 'yes' }
      }
      steps {
        script {
          env.DEPLOY_TO_STAGE = cicd.proceedPromptWithTimeout('stage')
        }
      }
    }

    stage('pre-deploy check') {
      agent any
      when {
        environment name: 'DEPLOY_TO_STAGE', value: 'yes'
      }
      steps {
        script {
          def deployed = deployedBranch()
          upToDate = upToDateWith('stage') || deployed == env.CHANGE_BRANCH
          if (!upToDate) {
            slackSend channel: 'fulfilment', color: 'warning', message: "Deploying <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> over `${deployed}` to staging :shipit_parrot:"
          }
        }
      }
    }

    // Deploy to staging
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

    // Deploy to prod:
    stage('deploy to prod') {
      when {
        branch 'master'
      }
      steps {
        measure {
          script {
            def contractTests = false
            def deploymentDirectory = ''
            def portyardConfig = ''
            def releaseName = ''
            def cicdConfig = ["SNYK_MONITOR":"true", "LOCAL_DOCKERFILE":"Dockerfile.prebuilt"]
            cicd.deploy('prod', deploymentDirectory, portyardConfig, releaseName, contractTests, cicdConfig)
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
        if (env.BRANCH_NAME == 'master') {
          slackSend channel: 'fulfilment', color: 'bad', message: "Failed to deploy <${env.RUN_DISPLAY_URL}|${env.JOB_NAME}> :cry:"
        }
      }
    }
  }
}


def upToDateWith(tag) {
  def upToDate = false
  try {
    def cicdGit = new com.depop.cicd.Git()
    cicdGit.createUpstream()
    sh "git fetch upstream refs/tags/${tag}"
    upToDate = sh(script: "git merge-base --is-ancestor ${tag} ${env.GIT_COMMIT}", returnStatus: true) == 0
  } catch (e) { echo "$e" }
  return upToDate
}

def deployedBranch() {
  def branch = ""
  try {
    serviceName = cicd.getServiceName()
    branch = sh(
      script: "curl -s https://cosmos.depop.com/api/v1/service/${serviceName} | jq -r '.deployments.staging[0].git_branch'",
      returnStdout: true
    )
  } catch (e) { echo "$e" }
  return branch.trim()
}
