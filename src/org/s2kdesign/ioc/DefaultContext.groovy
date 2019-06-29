package org.s2kdesign.ioc


import org.s2kdesign.IStepExecutor
import org.s2kdesign.StepExecutor

class DefaultContext implements IContext, Serializable {
    private _steps

    DefaultContext(steps) {
        this._steps = steps
    }

    @Override
    IStepExecutor getStepExecutor() {
        return new StepExecutor(this._steps)
    }
}
