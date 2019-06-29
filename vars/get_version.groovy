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
    def versionDevelop = libraryResource 'develop_version'
    def projectVersion = versionMaster.split('\\.')

    if (branchName == "master"){
        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${BUILD_NUMBER}.0"

        echo  "using version ${returnVersion}"
        return returnVersion
    } else if (branchName == "develop") {
        projectVersion = versionDevelop.split('\\.')

        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${BUILD_NUMBER}.0"

        echo  "using version ${returnVersion}"
        return returnVersion
    } else {
        def returnVersion = "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"

        echo  "using version ${returnVersion}"
        return returnVersion
    }
}