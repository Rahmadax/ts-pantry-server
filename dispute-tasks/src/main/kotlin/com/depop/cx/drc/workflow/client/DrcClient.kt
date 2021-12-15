package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*

private const val SET_DISPUTE_STATUS_URI = "/internal/v1/disputes/{disputeId}/status"
private const val SET_DISPUTE_PARTICIPANT = "/internal/v1/disputes/participants"

class DrcClient(webClient: WebClient) : Client(webClient) {

    fun setDisputeParticipant(disputeId: UUID, participantId: Long, participantRole: String): Mono<Void> {
        val request = SetDisputeParticipantRequest(disputeId, participantId, participantRole)
        return postRequest(SET_DISPUTE_PARTICIPANT, request)
    }

    fun setDisputeStatus(disputeId: UUID, disputeStatus: String): Mono<Void> {
        val request = SetDisputeStatusRequest(disputeStatus)
        return postRequest(SET_DISPUTE_STATUS_URI, request, mapOf("disputeId" to disputeId))
    }

}

data class SetDisputeParticipantRequest(
    @JsonProperty("dispute_id") val disputeId: UUID,
    @JsonProperty("user_id") val userId: Long,
    @JsonProperty("role") val role: String
)

data class SetDisputeStatusRequest(@JsonProperty("status") val disputeStatus: String)