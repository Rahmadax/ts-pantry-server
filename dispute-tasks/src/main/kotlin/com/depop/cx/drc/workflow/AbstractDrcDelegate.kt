package com.depop.cx.drc.workflow

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

enum class TaskErrorCode(val code: String) {
    FAILURE("failure")
}

abstract class AbstractDrcDelegate(private val taskMetrics: TaskMetrics) : JavaDelegate {

    private val logger = KotlinLogging.logger {}

    override fun execute(execution: DelegateExecution?) {
        val delegateMetadata = DelegateMetadata.from(execution, this::class.java)
        try {
            val timer = taskMetrics.getDelegateExecutionTimer(delegateMetadata)
            execution?.let {
                timer.record(Runnable {
                    doExecute(it)
                })
            }
        } catch (exception: Throwable) {
            logException(exception, execution, delegateMetadata)
            recordMetric(exception, delegateMetadata)
            throw exception
        }
    }

    private fun logException(
        exception: Throwable,
        delegateExecution: DelegateExecution?,
        delegateMetadata: DelegateMetadata
    ) {
        logger.error(exception) { "Failed to execute task ${delegateExecution?.id ?: "<unknown>"}: $delegateMetadata" }
    }

    private fun recordMetric(exception: Throwable, delegateMetadata: DelegateMetadata) {
        taskMetrics.getDelegateFailureCounter(delegateMetadata, exception::class.java).increment()
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