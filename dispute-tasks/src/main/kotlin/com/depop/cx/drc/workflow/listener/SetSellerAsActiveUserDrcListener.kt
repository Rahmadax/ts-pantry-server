package com.depop.cx.drc.workflow.listener

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.DrcClient
import com.depop.cx.drc.workflow.client.UpdateActiveUserIdDisputeRequest
import com.depop.cx.drc.workflow.getLongProcessVariableOrNull
import com.depop.cx.drc.workflow.getUUIDProcessVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val SELLER_ID_PROPERTY = "seller_id"
private const val DISPUTE_ACTIVE_USER = "active_user"

class SetSellerAsActiveUserDrcListener(
    private val drcClient: DrcClient,
    taskMetrics: TaskMetrics,
) : AbstractDrcDelegate(taskMetrics) {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val disputeId = execution.getUUIDProcessVariableOrNull(DISPUTE_ID_PROPERTY)
        if (disputeId == null) {
            logger.error { "Unable to find dispute ID in the task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$DISPUTE_ID_PROPERTY must not be null or empty."
            )
        }

        val sellerId = execution.getLongProcessVariableOrNull(SELLER_ID_PROPERTY)
        if (sellerId == null) {
            logger.error { "Unable to find seller ID in the task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$SELLER_ID_PROPERTY must not be null or empty."
            )
        }

        drcClient.updateDispute(
            disputeId,
            UpdateActiveUserIdDisputeRequest(sellerId)
        ).block()

        execution.setVariable(DISPUTE_ACTIVE_USER, sellerId.toString())
    }
}