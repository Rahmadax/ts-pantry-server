package com.depop.cx.drc.workflow.tasks.timer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

class ActivateTimerDelegate :AbstractTimerDelegate() {
    private val logger = KotlinLogging.logger {}
    override fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity) {
        logger.info { "Activating timer ${timer.jobHandlerConfigurationRaw} with id ${timer.id}" }
        execution.processEngineServices.managementService.activateJobById(timer.id)
    }
}