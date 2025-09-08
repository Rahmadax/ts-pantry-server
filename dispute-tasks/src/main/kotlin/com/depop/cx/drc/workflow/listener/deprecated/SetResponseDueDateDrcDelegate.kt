package com.depop.cx.drc.workflow.listener.deprecated

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.DrcClient
import com.depop.cx.drc.workflow.client.UpdateResponseDueAtDisputeRequest
import com.depop.cx.drc.workflow.getUUIDProcessVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.Expression
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val DISPUTE_RESPONSE_DUE_DATE = "response_due_date"

class SetResponseDueDateDrcDelegate(
    private val drcClient: DrcClient,
    private val taskMetrics: TaskMetrics,
) : AbstractDrcDelegate(taskMetrics) {

    lateinit var response_due_date: Expression

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {

        val responseDueDate = response_due_date.getValue(execution) as String

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")
        val localDateTime = LocalDateTime.ofInstant(Instant.parse(responseDueDate), ZoneOffset.UTC)

        val disputeId = execution.getUUIDProcessVariableOrNull(DISPUTE_ID_PROPERTY)

        if (disputeId == null) {
            logger.error { "Unable to find dispute ID in the task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$DISPUTE_ID_PROPERTY must not be null or empty."
            )
        }

        drcClient.updateDispute(
            disputeId,
            UpdateResponseDueAtDisputeRequest(localDateTime.format(formatter))
        ).block()

        execution.setVariable(DISPUTE_RESPONSE_DUE_DATE, responseDueDate)

    }
}