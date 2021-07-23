def call(body) {
    
    // evaluate the body block, and collect configuration into the object
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    // now build, based on the configuration provided
   pipeline {
        stage("Get ${projectName}.zip") {
                    parallel {
                        stage("Publish ${projectName} TESTS") {
                            when{
                                anyOf {
                                    expression{env.BUILD_NUMBER == '1'}
                                    changeset "${projectName}/**/*.*"
                                    expression{ currentBuild.previousSuccessfulBuild == null }
                                }
                            }
                            steps {
                                script{	
                                    bat  "\"${tool 'MsBuild'}\" ${command} "
                                }

                                zip zipFile: "${projectName}.zip", archive: false, dir: "${outputDir}"
                                archiveArtifacts artifacts: "${projectName}.zip", fingerprint: true
                            }
                        } 
                        stage("Get ${projectName}.zip from prevuis build") {
                            when{
                                not {
                                    anyOf {
                                        expression{env.BUILD_NUMBER == '1'}
                                        changeset "${projectName}/**/*.*"
                                        expression{ currentBuild.previousSuccessfulBuild == null }
                                    }      
                                }                
                            }
                            steps {     
                                copyArtifacts(projectName: currentBuild.projectName, filter: "${projectName}.zip", selector: lastSuccessful())
                                archiveArtifacts artifacts: "${projectName}.zip", caseSensitive: false
                            }          
                        } 
                    }
                }

            }
    
}