package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient

private const val GET_BLOCK_STATUS = "/internal/v1/users/{blockerUserId}/blocking-info/{targetUserId}/"

class BlockingClient(webClient: WebClient) : Client(webClient) {
    suspend fun getBlockStatus(blockerUserId: Long, targetUserId: Long): BlockStatus {
        return getCoroutineRequest(GET_BLOCK_STATUS,
            mapOf("blockerUserId" to blockerUserId, "targetUserId" to targetUserId))
    }
}

data class BlockStatus(
    @JsonProperty("blocked") val blocked: Boolean = true,
)