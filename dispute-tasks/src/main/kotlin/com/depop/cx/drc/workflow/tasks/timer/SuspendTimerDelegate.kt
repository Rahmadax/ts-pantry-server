package com.depop.cx.drc.workflow.tasks.timer

import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

class SuspendTimerDelegate :AbstractTimerDelegate() {
    private val logger = KotlinLogging.logger {}
    override fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity) {
        logger.info { "Suspending timer ${timer.jobHandlerConfigurationRaw} with id ${timer.id}" }
        execution.processEngineServices.managementService.suspendJobById(timer.id)
    }
}