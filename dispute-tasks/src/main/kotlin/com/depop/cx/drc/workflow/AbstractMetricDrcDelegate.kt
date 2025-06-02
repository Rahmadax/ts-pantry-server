package com.depop.cx.drc.workflow

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

//TODO: Make this the only ABC!
abstract class AbstractMetricDrcDelegate(
    private val taskMetrics: TaskMetrics,
) : JavaDelegate {

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

    private fun logException(exception: Throwable, delegateExecution: DelegateExecution?, failureMetadata: DelegateFailureMetadata) {
        logger.error(exception){ "Failed to execute task ${delegateExecution?.id ?: "<unknown>"}: $failureMetadata" } // TODO: structured logs or raise ticket
    }

    private fun recordMetric(failureMetadata: DelegateFailureMetadata) {
        taskMetrics.getDelegateFailureCounter(failureMetadata).increment()
    }

    abstract fun doExecute(execution: DelegateExecution)

}