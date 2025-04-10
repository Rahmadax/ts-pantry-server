package com.depop.cx.drc.workflow.client

import com.depop.cx.drc.workflow.tasks.LinkImagesDrcDelegate
import org.springframework.web.reactive.function.client.WebClient

private const val LINK = "/internal/v1/{entity_type}/{entity_id}/pictures/"

class PictureClient(webClient: WebClient) : Client(webClient) {

    suspend fun linkDisputeTaskImages(
        entityId: String,
        pictureIds: LinkImagesDrcDelegate.PictureIds,
    ): String {
        return putCoroutineRequest(LINK, pictureIds, mapOf(
            "entity_type" to "dispute-task",
            "entity_id" to entityId
        ))
    }
}