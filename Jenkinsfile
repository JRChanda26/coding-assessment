pipeline {
  agent any

  tools {
    maven "maven"
  }

  stages {
    stage('Install Dependencies') {
      steps {
        script {
           sh 'mvn clean install -DskipTests'
        }
      }
    }
stage('Docker Login') {
    steps {
        sh "echo 'Dev45#iosys89\$' | docker login -u iosysdev --password-stdin"
    }
}

    stage('Docker Build and Publish') {
      steps {
        script {
           env.BUILD_NUMBER = env.BUILD_NUMBER.trim()

           sh "docker build -t iosysdev/interviewservice:${BUILD_NUMBER} ."

           sh "docker tag iosysdev/interviewservice:${BUILD_NUMBER} iosysdev/interviewservice:${BUILD_NUMBER}"

           sh "docker push iosysdev/interviewservice:${BUILD_NUMBER}"
        }
      }
    }
    stage('Deploy') {
    steps {
        script {
             def containerInfo = sh(script: 'docker ps --format "{{.ID}} {{.Ports}}"', returnStdout: true).trim()

             if (containerInfo.contains('0.0.0.0:8082->')) {
                 def containerId = containerInfo.tokenize("\n").find { it.contains('0.0.0.0:8082->') }?.tokenize(" ")[0]

                 if (containerId) {
                    sh "docker stop $containerId"
                } else {
                    echo "No container found exposing port 8082"
                }
            }

             sh "docker run -d -p 8082:8082 iosysdev/interviewservice:${BUILD_NUMBER}"
        }
    }
}
  }
  // No changes needed in the post section
}
