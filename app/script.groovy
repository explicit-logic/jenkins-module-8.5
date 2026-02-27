
def incrementVersion() {
  echo 'incrementing app version'
  sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
  def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
  def version = matcher[0][1]

  env.VERSION = "$version-$BUILD_NUMBER"
}

def testApp() {
  echo 'testing the application...'
  sh 'mvn test'
}

def buildJar() {
  echo 'building the application...'
  sh 'mvn clean package'
}

def buildImage(String image) {
  echo "building the docker image..."
  withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
    sh "docker build -t ${image}:${VERSION} ."
    sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
    sh "docker push ${image}:${VERSION}"
  }
}

def deployApp() {
  echo 'deploying the application...'
}

def commitVersion() {
  withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
    sh 'git config --global user.email "jenkins@example.com"'
    sh 'git config --global user.name "jenkins"'

    sh "git status"
    sh "git branch"
    sh "git config --list"
    sh "git remote set-url origin https://${USERNAME}:${PASSWORD}@github.com/explicit-logic/jenkins-module-8.5"
    sh 'git add .'
    sh "git commit -m 'ci: version ${VERSION}'"
    sh 'git push origin HEAD:jenkins-jobs'
  }
}

return this
