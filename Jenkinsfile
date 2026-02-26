def gv

pipeline {
  agent any
  tools {
    maven 'maven-3.9'
  }
  parameters {
    string(name: 'DOCKER_IMAGE', defaultValue: 'explicitlogic/app')
  }
  stages {
    stage("init") {
      steps {
        dir('app') {
          script {
            gv = load "script.groovy"
          }
        }
      }
    }

    stage("increment version") {
      steps {
        dir('app') {
          script {
            gv.incrementVersion()
          }
        }
      }
    }

    stage("test") {
      steps {
        dir('app') {
          script {
            gv.testApp()
          }
        }
      }
    }

    stage("build jar") {
      steps {
        dir('app') {
          script {
            gv.buildJar()
          }
        }
      }
    }

    stage("build image") {
      steps {
        dir('app') {
          script {
            gv.buildImage(params.DOCKER_IMAGE)
          }
        }
      }
    }

    stage("deploy") {
      steps {
        dir('app') {
          script {
            gv.deployApp()
          }
        }
      }
    }

    stage("commit version update") {
      steps {
        dir('app') {
          script {
            gv.commitVersion()
          }
        }
      }
    }
  }
} 
