package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.getStringVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

private const val TIMER_ID_PARAM = "timer_id"

abstract class AbstractTimerDelegate(private val taskMetrics: TaskMetrics) : AbstractDrcDelegate(taskMetrics) {

    override fun doExecute(execution: DelegateExecution) {
        val timerId = execution.getStringVariableOrNull(TIMER_ID_PARAM)
        if (timerId == null) {
            throw IllegalArgumentException("variable $TIMER_ID_PARAM is not set")
        }

        val managementService = execution.processEngineServices.managementService

        val rawTimers =
            managementService.createJobQuery().processInstanceId(execution.processInstanceId).timers().list()
                .filterIsInstance<TimerEntity>()

        val filteredTimers = rawTimers.filter {
            timerId.equals(it.jobHandlerConfiguration.toCanonicalString())
        }

        when (filteredTimers.size) {
            1 -> doTimerExecute(execution, filteredTimers.single())
            else -> throw IllegalStateException("${filteredTimers.size} timers found with id $timerId")
        }
    }

    abstract fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity)

}
