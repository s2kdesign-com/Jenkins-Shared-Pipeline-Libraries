# Jenkins Shared Pipeline Libraries
Fork this repo and change the files in resources folder to configure all of your projects to have one version for each branch (helpful when multiple teams are releasing every 2 weeks and the version of all team projects should be controlled in one place )
```
resources/
    master_version  - 3.18.33
```
After that go to Jenkins -> Configure System and add the settings from the picture bellow
![Pipeline detail flow](docs/images/2019-06-29_18h01_13.png)

### Methods
GitFlow Versioning in Jenkinsfile:
```
- get_version(String branchName)
Support and Release branches are taken from branch name:
   - support/3.16.25 - *.**.**.BUILD_NUMBER = 3.16.25.1
   - support/3.17.25 - *.**.**.BUILD_NUMBER = 3.17.25.1
   - release/3.19.25 - *.**.**.BUILD_NUMBER = 3.19.25.0
   - release/4.0.33  - *.**.**.BUILD_NUMBER = 4.0.33.0

Defined versions use file master_version (3.19.33):
   - master Branch   - *.**.**.BUILD_NUMBER = 3.19.33.1
   - hotfix Branch   - *.**.**.BUILD_NUMBER = 3.19.33.0
   
This branches use file develop_version (4.0):
   - develop Branch  - *.**.**.BUILD_NUMBER = 4.0.33.1
   - feature Branch  - *.+1.**.BUILD_NUMBER = 4.1.33.1
```

![GitFlow](docs/images/2019-06-29_23h03_51.png)

Result Multibranch Pipeline:
 
![GitFlow](docs/images/2019-06-30_00h30_04.png)

MsBuild publishing for projects:
- TODO
 
.NET Core publishing for projects:
- TODO
### How to use it in your Jenkinsfile 

```Groovy
// add the following line and replace necessary values if you are not loading the library implicitly
// @Library(['github.com/Magik3a/Jenkinsfile_Gitflow_Versioning/org.s2kdesign']) _

pipeline {
 agent any
    environment {
        // Version is defined in external pipeline library https://github.com/Magik3a/Jenkinsfile_Gitflow_Versioning
        PROJECT_VERSION = get_version(GIT_BRANCH)
        PROJECT_PATH = "HiddenHook/HiddenHook.Web/HiddenHook.Web.csproj"
        PROJECT_NAME = "HiddenHook"  
     }
    stages {
            
        stage('Publish Project') {
            steps {
                publishProject()
            }
        }

        stage('Get Artifacts') {
            steps {
                zipArtifacts()
                getArtifacts()
            }        
        }
				
    }

    options {
        // make sure we only keep 50 builds at a time, so we don't fill up our storage!
        buildDiscarder(logRotator(numToKeepStr:'50'))
    }
}
def publishProject(){
    bat 'dotnet publish '+ PROJECT_PATH +' -c Debug -o ../../Publish/Debug /P:AssemblyVersion='+ PROJECT_VERSION +' /P:Version='+ PROJECT_VERSION 
    bat 'dotnet publish '+ PROJECT_PATH +' -c Release -o ../../Publish/Release /P:AssemblyVersion='+ PROJECT_VERSION +' /P:Version='+ PROJECT_VERSION 
     
}
def zipArtifacts(){
    zip zipFile: PROJECT_NAME +'Debug.zip', archive: false, dir: 'Publish/Debug'
    zip zipFile: PROJECT_NAME +'Release.zip', archive: false, dir: 'Publish/Release'
}
def getArtifacts(){    
    archiveArtifacts artifacts: PROJECT_NAME +'Debug.zip', fingerprint: true
    archiveArtifacts artifacts: PROJECT_NAME +'Release.zip', fingerprint: true
}
```