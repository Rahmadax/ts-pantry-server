package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.client.PictureClient
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.io.BufferedReader
import java.io.InputStreamReader

class LinkImagesTask(private val pictureClient: PictureClient) : AbstractTask() {
    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val taskService = execution.processEngineServices.taskService

        val task = taskService.createTaskQuery()
            .processInstanceId(execution.processInstanceId)
            .taskDefinitionKey(execution.currentActivityId)
            .singleResult()

        if (task == null) {
            logger.error { "Task not found for activity ID: ${execution.currentActivityId}" }
        } else {
            val taskId = task.id
            val attachments = taskService.getTaskAttachments(taskId)

            val latestPictureIds = attachments
                .filter { attachment -> attachment.type == "PICTURE" }
                .sortedByDescending { it.createTime }
                .take(5) // We can only link a maximum of 5 images to a dispute task
                .mapNotNull { attachment ->
                    taskService.getAttachmentContent(attachment.id)?.let { contentStream ->
                        BufferedReader(InputStreamReader(contentStream)).use { reader ->
                            val text = reader.readText()
                            // Filter out any bad data. Pictures may be dropped here
                            if (text.toLongOrNull() != null) text else null
                        }
                    }
                }

            runBlocking {
                pictureClient.linkDisputeTaskImages(taskId, PictureIds(latestPictureIds))
            }
        }
    }

    data class PictureIds(
        @JsonProperty("picture_ids")
        val pictureIds: List<String>
    )
}
