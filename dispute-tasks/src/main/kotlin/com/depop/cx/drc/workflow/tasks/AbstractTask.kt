package com.depop.cx.drc.workflow.tasks

import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
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
        logger.error(exception) { "Failed to execute task ${delegateExecution?.id ?: "<unknown>"}" }
    }

    protected fun validate(key: String, value: Any?) {
        if(value == null) {
            logger.error { "Unable to find $key task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$key must not be null or empty."
            )
        }
    }

    abstract fun doExecute(execution: DelegateExecution)

}