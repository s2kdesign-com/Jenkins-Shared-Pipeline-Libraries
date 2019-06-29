package org.s2kdesign.ioc


import org.s2kdesign.IStepExecutor

interface IContext {
    IStepExecutor getStepExecutor()
}
