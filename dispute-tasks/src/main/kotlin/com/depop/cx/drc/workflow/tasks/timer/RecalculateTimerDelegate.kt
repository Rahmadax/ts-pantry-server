package com.depop.cx.drc.workflow.tasks.timer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

class RecalculateTimerDelegate : AbstractTimerDelegate() {
    private val logger = KotlinLogging.logger {}
    override fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity) {
        logger.info { "Recalculating timer ${timer.jobHandlerConfigurationRaw} with id ${timer.id}" }
        execution.processEngineServices.managementService.recalculateJobDuedate(timer.id, false)
    }
}