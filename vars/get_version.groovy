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

    if (branchName == "master"){
        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    } 
    else if (branchName.startsWith("hotfix")) {
        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    }
    else if (branchName == "develop") {
        def returnVersion = "${projectVersion[0]}.${projectVersion[1] + 1}.${projectVersion[2] + 1}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    } 
    else if (branchName.startsWith("release")) {
        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2] + 1}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    }
    else if (branchName.startsWith("support"))
    {
        def branchVersion =   branchName.split('/')[1].split('.');
        def returnVersion = "${branchVersion[0]}.${branchVersion[1]}.${BUILD_NUMBER}.0"

        echo  "using version ${returnVersion}"
        return returnVersion
    }
    else {
        def returnVersion = "${projectVersion[0] + 1}.${projectVersion[1] + 1}.${projectVersion[2] + 1}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    }
}