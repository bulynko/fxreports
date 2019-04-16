pipeline {
    agent {
        docker {
            image 'maven:3-alpine' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Initialize'){
            def dockerHome = tool name: 'docker198', type: 'org.jenkinsci.plugins.docker.commons.tools.DockerTool'
            env.PATH = "${dockerHome}/bin:${env.PATH}"
        }

        stage('Build') { 
            steps {
                sh 'mvn -B -DskipTests clean package' 
            }
        }
    }
}
