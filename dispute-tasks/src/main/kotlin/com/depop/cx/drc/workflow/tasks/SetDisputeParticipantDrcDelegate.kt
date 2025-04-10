package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.*
import com.depop.cx.drc.workflow.client.DrcClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val PARTICIPANT_ID_PROPERTY = "participant_id"
private const val PARTICIPANT_ROLE_PROPERTY = "participant_role"

class SetDisputeParticipantDrcDelegate(private val drcClient : DrcClient) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {

        val disputeId = execution.getUUIDVariableOrNull(DISPUTE_ID_PROPERTY)
        val participantId = execution.getLongVariableOrNull(PARTICIPANT_ID_PROPERTY)
        val participantRole = execution.getStringVariableOrNull(PARTICIPANT_ROLE_PROPERTY)

        if (disputeId != null && participantId != null && participantRole != null) {

            logger.debug { "Setting participant $participantId with role $participantRole on dispute $disputeId." }
            drcClient.setDisputeParticipant(disputeId, participantId, participantRole).block()

        } else {
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$PARTICIPANT_ID_PROPERTY and $PARTICIPANT_ROLE_PROPERTY must not be null or empty.")
        }

    }

}