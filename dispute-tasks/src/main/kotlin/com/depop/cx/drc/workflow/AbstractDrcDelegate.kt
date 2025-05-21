package com.depop.cx.drc.workflow

import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

enum class TaskErrorCode(val code: String) {
    FAILURE("failure")
}

abstract class AbstractDrcDelegate : JavaDelegate {

    private val logger = KotlinLogging.logger {}


    override fun execute(execution: DelegateExecution?) {
        try {
            execution?.apply { doExecute(this) }
        } catch (exception: Throwable) {
            handleException(exception, execution)
            throw exception
        }
    }

    private fun handleException(exception: Throwable, delegateExecution: DelegateExecution?) {
        logger.error(exception) { "Failed to execute task ${delegateExecution?.id ?: "<unknown>"}" }
        delegateExecution?.createIncident("failedJob", exception.message)
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