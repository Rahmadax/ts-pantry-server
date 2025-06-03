package com.depop.cx.drc.workflow

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.beans.factory.annotation.Autowired

enum class TaskErrorCode(val code: String) {
    FAILURE("failure")
}

abstract class AbstractDrcDelegate(private val taskMetrics: TaskMetrics) : JavaDelegate {

    private val logger = KotlinLogging.logger {}

    override fun execute(execution: DelegateExecution?) {
        try {
            execution?.apply { doExecute(this) }
        } catch (exception: Throwable) {
            val failureMetadata = DelegateFailureMetadata.from(execution, exception, this::class.java)
            logException(exception, execution, failureMetadata)
            recordMetric(failureMetadata)
            throw exception
        }
    }

    private fun logException(
        exception: Throwable,
        delegateExecution: DelegateExecution?,
        failureMetadata: DelegateFailureMetadata
    ) {
        logger.error(exception) { "Failed to execute task ${delegateExecution?.id ?: "<unknown>"}: $failureMetadata" }
    }

    private fun recordMetric(failureMetadata: DelegateFailureMetadata) {
        taskMetrics.getDelegateFailureCounter(failureMetadata).increment()
    }

    protected fun validate(key: String, value: Any?) {
        if (value == null) {
            logger.error { "Unable to find $key task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$key must not be null or empty."
            )
        }
    }

    abstract fun doExecute(execution: DelegateExecution)

}