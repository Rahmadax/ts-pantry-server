package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.client.CommsClient
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val RECIPIENT_ID_PROPERTY = "recipient_id"
private const val TEMPLATE_ID_PROPERTY = "template_id"

class SendChatTask(
    private val commsClient: CommsClient,
) : AbstractTask() {

    override fun doExecute(execution: DelegateExecution) {
        val recipientId = execution.getLongVariableOrNull(RECIPIENT_ID_PROPERTY)
        val templateId = execution.getUUIDVariableOrNull(TEMPLATE_ID_PROPERTY)
        validate(RECIPIENT_ID_PROPERTY, recipientId)
        validate(TEMPLATE_ID_PROPERTY, templateId)
        commsClient.sendChat(recipientId!!, templateId!!, execution.variables).block()
    }

}