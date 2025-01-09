package com.depop.cx.drc.workflow.client

import com.depop.cx.drc.workflow.tasks.LinkImagesTask
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val LINK = "/internal/v2/{entity_type}/{entity_id}/pictures/"

class PictureClient(webClient: WebClient) : Client(webClient) {

    suspend fun linkImages(
        taskId: String,
        processDefinitionId: String,
        pictureIds: LinkImagesTask.PictureIds,
    ): String {
        return putCoroutineRequest(LINK, pictureIds, mapOf(
            "entity_type" to "products", // TODO: Update to dispute task when done testing
            "entity_id" to "$processDefinitionId:$taskId"
        ))
    }
}