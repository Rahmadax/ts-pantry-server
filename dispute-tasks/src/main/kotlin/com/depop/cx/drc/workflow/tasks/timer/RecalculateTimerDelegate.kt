package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

class RecalculateTimerDelegate(private val taskMetrics: TaskMetrics) : AbstractTimerDelegate(taskMetrics) {
    private val logger = KotlinLogging.logger {}
    override fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity) {
        logger.info { "Recalculating timer ${timer.jobHandlerConfigurationRaw} with id ${timer.id}" }
        execution.processEngineServices.managementService.recalculateJobDuedate(timer.id, false)
    }
}