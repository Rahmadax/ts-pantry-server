package com.depop.cx.drc.workflow.tasks.comms

import com.depop.cx.drc.workflow.client.Channel
import com.depop.cx.drc.workflow.client.CommsClient
import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.getUUIDVariableOrNull
import org.camunda.bpm.engine.TaskService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.commons.utils.IoUtil

private const val RECIPIENT_ID_PROPERTY = "recipient_id"
private const val TEMPLATE_ID_PROPERTY = "template_id"

abstract class AbstractCommsDrcDelegate(
    private val commsClient: CommsClient,
) : AbstractDrcDelegate() {

    override fun doExecute(execution: DelegateExecution) {
        val recipientId = execution.getLongVariableOrNull(RECIPIENT_ID_PROPERTY)
        val templateId = execution.getUUIDVariableOrNull(TEMPLATE_ID_PROPERTY)
        val processId = execution.processInstanceId
        val taskId = execution.id
        validate(RECIPIENT_ID_PROPERTY, recipientId)
        validate(TEMPLATE_ID_PROPERTY, templateId)
        val content = commsClient.send(
            getChannel(),
            recipientId!!,
            templateId!!,
            execution.variables,
            taskId
        ).block()
        createAttachment(execution.processEngine.taskService, taskId, processId, content)
    }

    private fun createAttachment(
        taskService: TaskService,
        processId: String,
        taskId: String,
        content: String?
    ) {
        if (content != null) {
            val stream = IoUtil.stringAsInputStream(content)
            taskService.createAttachment(
                getChannel().channel,
                taskId,
                processId,
                "content",
                "",
                stream
            )
        }
    }

    protected abstract fun getChannel(): Channel

}