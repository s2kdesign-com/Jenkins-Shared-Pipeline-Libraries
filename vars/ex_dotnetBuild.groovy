import org.s2kdesign.build.MsBuild
import org.s2kdesign.ioc.ContextRegistry

/**
 * Example custom step for easy use of dotnet build inside Jenkinsfiles
 * @param solutionPath Path to .sln file
 * @return
 */
def call(String projectName, String solutionPath) {
    pipeline {
    agent any
        environment {
            // Version is defined in external pipeline library https://github.com/Magik3a/Jenkinsfile_Gitflow_Versioning
            PROJECT_VERSION = get_version(GIT_BRANCH)
            PROJECT_PATH = solutionPath
            PROJECT_NAME = projectName
            }
        stages {
                
            stage('Publish Project') {
                steps {                    
                    bat 'dotnet publish '+ PROJECT_PATH +' -c Debug -o ../../Publish/Debug /P:AssemblyVersion='+ PROJECT_VERSION +' /P:Version='+ PROJECT_VERSION 
                    bat 'dotnet publish '+ PROJECT_PATH +' -c Release -o ../../Publish/Release /P:AssemblyVersion='+ PROJECT_VERSION +' /P:Version='+ PROJECT_VERSION 
                }
            }

            stage('Get Artifacts') {
                steps {
                    
                    zip zipFile: PROJECT_NAME +'Debug.zip', archive: false, dir: 'Publish/Debug'
                    zip zipFile: PROJECT_NAME +'Release.zip', archive: false, dir: 'Publish/Release'
                    
                    archiveArtifacts artifacts: PROJECT_NAME +'Debug.zip', fingerprint: true
                    archiveArtifacts artifacts: PROJECT_NAME +'Release.zip', fingerprint: true
                }        
            }
                    
        }

        options {
            // make sure we only keep 50 builds at a time, so we don't fill up our storage!
                buildDiscarder(logRotator(numToKeepStr:'50'))
        }
    }
}