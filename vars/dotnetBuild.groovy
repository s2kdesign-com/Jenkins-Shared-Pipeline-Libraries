def call(body) {
    
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // now build, based on the configuration provided
    stage('Publish Project') {
        steps {                    
            bat 'dotnet publish '+ PROJECT_PATH +' -c Debug -o Publish/Debug /P:PublishWithAspNetCoreTargetManifest=false /P:AssemblyVersion='+ PROJECT_VERSION +' /P:Version='+ PROJECT_VERSION 
            bat 'dotnet publish '+ PROJECT_PATH +' -c Release -o Publish/Release /P:PublishWithAspNetCoreTargetManifest=false /P:AssemblyVersion='+ PROJECT_VERSION +' /P:Version='+ PROJECT_VERSION 
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

    options {
        // make sure we only keep 50 builds at a time, so we don't fill up our storage!
            buildDiscarder(logRotator(numToKeepStr:'50'))
    }
    
}