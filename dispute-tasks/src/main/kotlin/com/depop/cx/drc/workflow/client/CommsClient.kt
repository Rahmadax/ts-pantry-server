package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

private const val SEND = "/send"

class CommsClient(webClient: WebClient) : Client(webClient) {

    fun send(
        channel: Channel,
        recipientId: Long,
        templateId: UUID,
        context: Map<String, Any>,
        idempotencyKey: String = UUID.randomUUID().toString()
    ): Mono<String> {
        val template = Template(templateId = templateId, channel = channel)
        val delivery = Delivery(recipient = recipientId, channel = channel, idempotencyKey = idempotencyKey)
        val request = SendRequest(template, context, delivery)
        return postRequest(SEND, request)
    }

}

data class SendRequest(
    @JsonProperty("template") val template: Template,
    @JsonProperty("placeholders") val placeholders: Map<String, Any>,
    @JsonProperty("delivery") val delivery: Delivery,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Template(
    @JsonProperty("id") val templateId: UUID,
    @JsonProperty("channel") val channel: Channel,
    @JsonProperty("version") val version: Long? = null,
    @JsonProperty("language") val language: Locale = Locale.UK,
    @JsonProperty("feature_set") val featureSet: Long? = null,
)

data class Delivery(
    @JsonProperty("channel") val channel: Channel,
    @JsonProperty("recipient_user_id") val recipient: Long,
    @JsonProperty("idempotency_key") val idempotencyKey: String,
)

enum class Channel(@JsonValue val channel: String) {
    EMAIL("email"),
    CHAT("chat"),
    PUSH("push")
}
