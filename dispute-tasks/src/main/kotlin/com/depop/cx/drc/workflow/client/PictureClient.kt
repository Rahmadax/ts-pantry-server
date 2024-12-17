package com.depop.cx.drc.workflow.client

import com.depop.cx.drc.workflow.tasks.LinkImagesTask
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val LINK = "/internal/v1/{entity_type}/{entity_id}/pictures/"

class PictureClient(webClient: WebClient) : Client(webClient) {

    fun linkImages(
        taskId: String,
        processDefinitionId: String,
        pictureIds: LinkImagesTask.PictureIds,
    ): Mono<String> {
        return postRequest(LINK, pictureIds, mapOf(
            "entity_type" to "products", // TODO: Update to dispute task when done testing
            "entity_id" to "$processDefinitionId:$taskId")
        )
    }
}