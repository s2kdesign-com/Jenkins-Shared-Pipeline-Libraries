import org.s2kdesign.IStepExecutor
import org.s2kdesign.ioc.ContextRegistry

/**
 * Getting default version depending on the branch. Versions are configured in resource folder
 * @param branchName Use GIT_BRANCH for Multibranch Pipeline Job
 * @return
 */
def call(String branchName) {
    ContextRegistry.registerDefaultContext(this)


    if (branchName == "master"){
        def versionMaster = libraryResource 'master_version'
        def projectVersion = versionMaster.split('\\.')

        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${BUILD_NUMBER}.0"

        echo  "using version ${returnVersion}"
        return returnVersion
    } else if (branchName.startsWith("hotfix")) {
        def versionMaster = libraryResource 'master_version'
        def projectVersion = versionMaster.split('\\.')

        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    }else if (branchName == "develop") {
        def versionDevelop = libraryResource 'develop_version'
        projectVersion = versionDevelop.split('\\.')

        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${BUILD_NUMBER}.0"

        echo  "using version ${returnVersion}"
        return returnVersion
    }  else {
        def versionDevelop = libraryResource 'develop_version'

        projectVersion = versionDevelop.split('\\.')
        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    }
}