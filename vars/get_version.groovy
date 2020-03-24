import org.s2kdesign.IStepExecutor
import org.s2kdesign.ioc.ContextRegistry

/**
 * Getting default version depending on the branch. Versions are configured in resource folder
 * @param branchName Use GIT_BRANCH for Multibranch Pipeline Job
 * @return
 */
def call(String branchName) {
    ContextRegistry.registerDefaultContext(this)

    def versionMaster = libraryResource 'master_version'
    def projectVersion = versionMaster.split('\\.').collect{it as int}      

    def returnVersion = "${projectVersion[0] + 1}.${projectVersion[1] + 1}.${projectVersion[2]}.${BUILD_NUMBER}"

    if (branchName == "master"){
        returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"
    } 
    else if (branchName.startsWith("hotfix")) {
        returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"
    }
    else if (branchName == "develop") {
        returnVersion = "${projectVersion[0]}.${projectVersion[1] + 1}.${projectVersion[2]}.${BUILD_NUMBER}"
    } 
    else if (branchName.startsWith("release")) {
        def branchVersion =   branchName.split('\\/')[1].split('\\.');
        returnVersion = "${branchVersion[0]}.${branchVersion[1]}.${branchVersion[2]}.${BUILD_NUMBER}"
    }
    else if (branchName.startsWith("support"))
    {
        def branchVersion =   branchName.split('\\/')[1].split('\\.');
        returnVersion = "${branchVersion[0]}.${branchVersion[1]}.${branchVersion[2]}.${BUILD_NUMBER}"
    }
    else {
        returnVersion = "${projectVersion[0] + 1}.${projectVersion[1] + 1}.${projectVersion[2]}.${BUILD_NUMBER}"
    }

    echo  "Building version ${returnVersion}"

    currentBuild.rawBuild.project.setDisplayName("${returnVersion}")
    return returnVersion
}

return this