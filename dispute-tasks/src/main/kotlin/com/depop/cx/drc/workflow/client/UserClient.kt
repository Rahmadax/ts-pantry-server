package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val GET_USER = "/internal/v1/users/{userId}/depop/"

class UserClient(webClient: WebClient) : Client(webClient) {

    fun getUser(userId: Long): Mono<User> {
        return getRequest(GET_USER, mapOf("userId" to userId))
    }
}

data class User(
    @JsonProperty("purchase_internal_id") val id: Long,
    @JsonProperty("is_active") val isActive: Boolean
) {
    fun isBanned(): Boolean = !isActive
}