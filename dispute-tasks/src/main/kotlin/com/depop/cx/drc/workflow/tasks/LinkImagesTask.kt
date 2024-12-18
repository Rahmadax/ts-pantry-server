package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.client.PictureClient
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.io.BufferedReader
import java.io.InputStreamReader

class LinkImagesTask(private val pictureClient: PictureClient) : AbstractTask() {
    override fun doExecute(execution: DelegateExecution) {
        val taskService = execution.processEngine.taskService
        val taskId = execution.id

        val latestPictureIds = taskService.getTaskAttachments(execution.id)
            .filter { attachment -> attachment.type == "PICTURE" }
            .sortedByDescending { it.createTime }
            .take(5) // We can only link a maximum of 5 images to a dispute task
            .mapNotNull { attachment ->
                taskService.getAttachmentContent(attachment.id)?.let { contentStream ->
                    BufferedReader(InputStreamReader(contentStream)).use { reader ->
                        reader.readText()
                    }
                }
            }

        pictureClient.linkImages(
            taskId,
            execution.processDefinitionId,
            PictureIds(latestPictureIds)
        ).block()
    }

    data class PictureIds(
        val pictureIds: List<Any>
    )
}