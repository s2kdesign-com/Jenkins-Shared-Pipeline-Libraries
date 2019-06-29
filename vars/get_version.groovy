import org.s2kdesign.IStepExecutor
import org.s2kdesign.ioc.ContextRegistry

/**
 * Getting default version depending on the branch. Versions are configured in resource folder 
 * @param branchName Use GIT_BRANCH for Multibranch Pipeline Job
 * @return
 */
def call(String branchName) {
    ContextRegistry.registerDefaultContext(this)

    def versionMaster = libraryResource 'master_version';
    def projectVersion = versionMaster.split('\\.');

    if (branchName == "master"){
        return "${projectVersion[0]}.${projectVersion[1]}.${BUILD_NUMBER}.0"
    } else if (branchName == "develop") {
        def versionDevelop = libraryResource 'develop_version';
        projectVersion = versionDevelop.split('\\.');
        return "${projectVersion[0]}.${projectVersion[1]}.${BUILD_NUMBER}.0"
    } else {
        return "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${BUILD_NUMBER}"
    }
}