package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.getStringVariableOrNull
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity
import org.camunda.bpm.model.bpmn.Bpmn

private const val TIMER_ID_PARAM = "timer_id"

abstract class AbstractTimerDelegate : AbstractDrcDelegate() {

    override fun doExecute(execution: DelegateExecution) {
        val timerId = execution.getStringVariableOrNull(TIMER_ID_PARAM)
        if (timerId == null) {
            throw BpmnError(TaskErrorCode.FAILURE.code, "variable $TIMER_ID_PARAM is not set")
        }

        val managementService = execution.processEngineServices.managementService
        val timers = managementService
            .createJobQuery()
            .processInstanceId(execution.processInstanceId)
            .timers()
            .list()
            .filterIsInstance<TimerEntity>()
            .filter { timerId.equals(it.jobHandlerConfiguration.toCanonicalString()) }

        when (timers.size) {
            1 -> doTimerExecute(execution, timers.single())
            else -> throw BpmnError(TaskErrorCode.FAILURE.code, "${timers.size} timers found with id $timerId")
        }
    }

    abstract fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity)

}