package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.DrcClient
import com.depop.cx.drc.workflow.client.UpdateResponseDueAtDisputeRequest
import com.depop.cx.drc.workflow.getStringProcessVariableOrNull
import com.depop.cx.drc.workflow.getStringVariableOrNull
import com.depop.cx.drc.workflow.getUUIDProcessVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import com.depop.cx.drc.workflow.tasks.UpdateContextDrcDelegate
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val ISO_RESPONSE_DUE_DATE_PROPERTY = "response_due_date"
private const val SHOULD_UPDATE_DRC_PARAM = "should_update_drc"

class UpdateResponseDueDateDelegate(private val drcClient: DrcClient, private val taskMetrics: TaskMetrics) : AbstractTimerDelegate(taskMetrics) {
    companion object {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
    }

    private val logger = KotlinLogging.logger {}

    override fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity) {
        val dueDateInstant = timer.duedate.toInstant()
        val dueDateString = OffsetDateTime.ofInstant(
            timer.duedate.toInstant(),
            ZoneId.of("UTC")
        ).toString()

        execution.setVariableLocal(ISO_RESPONSE_DUE_DATE_PROPERTY, dueDateString)

        UpdateContextDrcDelegate.updateContext(execution, mapOf(ISO_RESPONSE_DUE_DATE_PROPERTY to dueDateString))

        val shouldUpdatedDrc = execution.getStringVariableOrNull(SHOULD_UPDATE_DRC_PARAM)
        if (shouldUpdatedDrc == "true") {
            updateDrc(execution, dueDateInstant)
        }
    }

    private fun updateDrc(execution: DelegateExecution, dueDateInstant: Instant) {
        val disputeId = execution.getUUIDProcessVariableOrNull(DISPUTE_ID_PROPERTY)

        if (disputeId == null) {
            logger.error { "Unable to find dispute ID in the task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$DISPUTE_ID_PROPERTY must not be null or empty."
            )
        }

        val localDateTime = LocalDateTime.ofInstant(dueDateInstant, ZoneOffset.UTC)
        drcClient.updateDispute(
            disputeId,
            UpdateResponseDueAtDisputeRequest(localDateTime.format(formatter))
        ).block()
    }
}