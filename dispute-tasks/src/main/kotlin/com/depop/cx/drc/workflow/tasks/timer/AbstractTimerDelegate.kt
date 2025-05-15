package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.getStringVariableOrNull
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

private const val TIMER_KEY_PARAM = "timer"

abstract class AbstractTimerDelegate : AbstractDrcDelegate() {

    override fun doExecute(execution: DelegateExecution) {
        val timerName = execution.getStringVariableOrNull(TIMER_KEY_PARAM)
        if(timerName != null) {
            val managementService = execution.processEngineServices.managementService
            managementService
                .createJobQuery()
                .processInstanceId(execution.processInstanceId)
                .timers()
                .list()
                .filterIsInstance<TimerEntity>()
                .filter { timerName.equals(it.jobHandlerConfiguration.toCanonicalString()) }
                .forEach { doTimerExecute(execution, it) }
        }
    }

    abstract fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity)

}