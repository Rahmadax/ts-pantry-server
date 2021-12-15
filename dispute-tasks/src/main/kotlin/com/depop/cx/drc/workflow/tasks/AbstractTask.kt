package com.depop.cx.drc.workflow.tasks

import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

enum class TaskErrorCode(val code: String) {
    FAILURE("failure")
}

abstract class AbstractTask : JavaDelegate {

    private val logger = KotlinLogging.logger {}

    override fun execute(execution: DelegateExecution?) {
        try {
            execution?.apply { doExecute(this) }
        } catch (exception: Exception) {
            handleException(exception, execution)
            throw exception
        }
    }

    private fun handleException(exception: Exception, delegateExecution: DelegateExecution?) {
        logger.error("Failed to execute task ${delegateExecution?.id ?: "<unknown>"}", exception)
    }

    abstract fun doExecute(execution: DelegateExecution)

}