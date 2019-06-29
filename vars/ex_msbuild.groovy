import org.s2kdesign.build.MsBuild
import org.s2kdesign.ioc.ContextRegistry

/**
 * Example custom step for easy use of MsBuild inside Jenkinsfiles
 * @param solutionPath Path to .sln file
 * @return
 */
def call(String solutionPath) {
    ContextRegistry.registerDefaultContext(this)

    def msbuilder = new MsBuild(solutionPath)
    msbuilder.build()
}