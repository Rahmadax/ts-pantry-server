package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.DrcClient
import com.depop.cx.drc.workflow.getStringVariableOrNull
import com.depop.cx.drc.workflow.getUUIDVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val DISPUTE_STATUS_PROPERTY = "dispute_status"

class SetDisputeStatusDrcDelegate(private val drcClient: DrcClient) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {

        val disputeId = execution.getUUIDVariableOrNull(DISPUTE_ID_PROPERTY)
        val disputeStatus = execution.getStringVariableOrNull(DISPUTE_STATUS_PROPERTY)

        if (disputeId != null && disputeStatus != null) {

            logger.info { "Setting status $disputeStatus on dispute $disputeId." }

            drcClient.setDisputeStatus(disputeId, disputeStatus).block()
            execution.processInstance.setVariableLocal(DISPUTE_STATUS_PROPERTY, disputeStatus)

        } else {
            logger.error { "Unable to find dispute ID or status in the task context." }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$DISPUTE_ID_PROPERTY and $DISPUTE_STATUS_PROPERTY must not be null or empty."
            )
        }

    }
}
