import org.s2kdesign.ioc.ContextRegistry

/**
 * Example custom step for easy use of MsBuild inside Jenkinsfiles
 * @param solutionPath Path to .sln file
 * @return
 */
def call(String branchName) {
    ContextRegistry.registerDefaultContext(this)

    def versionMaster = libraryResource 'org/s2kdesign/master_version.json';
    def versionDevelop = libraryResource 'DEVELOP_VERSION';

    def projectVersion = versionMaster.split('\\.');

    if (branchName == "master"){
        return "${projectVersion[0]}.${projectVersion[1]}.${env.BUILD_NUMBER}.0"
    } else if (branchName == "develop") {
        return "${projectVersion[0]}.${projectVersion[1]}.${env.BUILD_NUMBER}.0"
    } else {
        return "${projectVersion[0]}.${projectVersion[1]}.${projectVersion[2]}.${env.BUILD_NUMBER}"
    }
}