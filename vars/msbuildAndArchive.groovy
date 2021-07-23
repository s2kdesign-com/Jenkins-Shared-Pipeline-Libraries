def call(body) {
    
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // now build, based on the configuration provided
   pipeline {
            stage("Publish ${config.projectName} TESTS") {
                when{
                    anyOf {
                        expression{env.BUILD_NUMBER == '1'}
                        changeset "${config.regex}"
                        expression{ currentBuild.previousSuccessfulBuild == null }
                    }
                }
                steps {
                    script{	
                        bat  "\"${tool 'MsBuild'}\" ${config.command} "
                    }

                    zip zipFile: "${config.projectName}.zip", archive: false, dir: "${config.outputDir}"
                    archiveArtifacts artifacts: "${config.projectName}.zip", fingerprint: true
                }
            } 
            stage("Get ${config.projectName}.zip from prevuis build") {
                when{
                    not {
                        anyOf {
                            expression{env.BUILD_NUMBER == '1'}
                            changeset "${config.regex}"
                            expression{ currentBuild.previousSuccessfulBuild == null }
                        }      
                    }                
                }
                steps {     
                    copyArtifacts(projectName: currentBuild.projectName, filter: "${config.projectName}.zip", selector: lastSuccessful())
                    archiveArtifacts artifacts: "${config.projectName}.zip", caseSensitive: false
                }          
            } 
             

        }
    
}