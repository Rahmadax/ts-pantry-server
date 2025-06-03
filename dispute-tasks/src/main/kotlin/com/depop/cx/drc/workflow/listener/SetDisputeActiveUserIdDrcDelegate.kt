package com.depop.cx.drc.workflow.listener

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.DrcClient
import com.depop.cx.drc.workflow.client.UpdateActiveUserIdDisputeRequest
import com.depop.cx.drc.workflow.getUUIDProcessVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.Expression

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val DISPUTE_ACTIVE_USER = "active_user"

class SetDisputeActiveUserIdDrcDelegate(
    private val drcClient: DrcClient,
    private val taskMetrics: TaskMetrics,
) : AbstractDrcDelegate(taskMetrics) {

    lateinit var active_user: Expression

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {

        val auid = active_user.getValue(execution) as String

        val disputeId = execution.getUUIDProcessVariableOrNull(DISPUTE_ID_PROPERTY)

        if (disputeId == null) {
            logger.error { "Unable to find dispute ID in the task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$DISPUTE_ID_PROPERTY must not be null or empty."
            )
        }

        val activeUserIdLong = auid.toLongOrNull() ?: throw BpmnError(
            TaskErrorCode.FAILURE.code,
            "Invalid Active User Id: $auid. It must be a valid Long value."
        )

        drcClient.updateDispute(
            disputeId,
            UpdateActiveUserIdDisputeRequest(activeUserIdLong)
        ).block()

        // Needs to be the string version of this value
        execution.setVariable(DISPUTE_ACTIVE_USER, auid)
    }
}