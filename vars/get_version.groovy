import org.s2kdesign.IStepExecutor
import org.s2kdesign.ioc.ContextRegistry

/**
 * Getting default version depending on the branch. Versions are configured in resource folder
 * @param branchName Use GIT_BRANCH for Multibranch Pipeline Job
 * @return
 */
def call(String branchName, String defaultVersion = "") {
    ContextRegistry.registerDefaultContext(this)
    if(defaultVersion == ""){
        defaultVersion = libraryResource 'master_version'
    }
    
    def versionMaster = defaultVersion.split('\\.').collect{it as int}      


    def returnVersion = "${versionMaster[0] + 1}.${versionMaster[1] + 1}.${versionMaster[2]}.${BUILD_NUMBER}"

    if (branchName == "master"){
        returnVersion = "${versionMaster[0]}.${versionMaster[1]}.${versionMaster[2]}.${BUILD_NUMBER}"
    } 
    else if (branchName.startsWith("hotfix")) {
        returnVersion = "${versionMaster[0]}.${versionMaster[1]}.${versionMaster[2]}.${BUILD_NUMBER}"
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
    else if (branchName == "develop") {
        // We use versionDevelop for developMajor.developMinor.masterSprintNumber
        returnVersion = "${versionMaster[0]}.${versionMaster[1] + 1}.${versionMaster[2]}.${BUILD_NUMBER}"
    } 
    else {
        // feature and other shits
        returnVersion = "${versionMaster[0]}.${versionMaster[1] + 1}.${versionMaster[2]}.${BUILD_NUMBER}"
    }

    echo  "Building version ${returnVersion}"

    currentBuild.displayName = "${returnVersion}"
    return returnVersion
}
