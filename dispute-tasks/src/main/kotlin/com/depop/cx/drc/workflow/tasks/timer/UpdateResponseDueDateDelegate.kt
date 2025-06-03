package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity
import java.time.OffsetDateTime
import java.time.ZoneId

private const val ISO_RESPONSE_DUE_DATE_PROPERTY = "response_due_date"

class UpdateResponseDueDateDelegate(private val taskMetrics: TaskMetrics) : AbstractTimerDelegate(taskMetrics) {
    override fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity) {
        val dueDate = OffsetDateTime.ofInstant(
            timer.duedate.toInstant(),
            ZoneId.of("UTC")
        )
        execution.setVariableLocal(ISO_RESPONSE_DUE_DATE_PROPERTY, dueDate.toString())
    }
}