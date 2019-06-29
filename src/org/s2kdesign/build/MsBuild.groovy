package org.s2kdesign.build


import org.s2kdesign.ioc.ContextRegistry
import org.s2kdesign.IStepExecutor

/**
 * Example class (without proper implementation) for using the MsBuild tool for building .NET projects.
 */
class MsBuild implements Serializable {
    private String _solutionPath

    MsBuild(String solutionPath) {
        _solutionPath = solutionPath
    }

    void build() {
        IStepExecutor steps = ContextRegistry.getContext().getStepExecutor()

        int returnStatus = steps.sh("echo \"building ${this._solutionPath}...\"")
        if (returnStatus != 0) {
            steps.error("Some error")
        }
    }
}
