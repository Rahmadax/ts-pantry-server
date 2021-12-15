package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val GET_PAYMENT_DETAILS = "/internal/v1/payments/{paymentId}/"

class PaymentsClient(webClient: WebClient) : Client(webClient) {

    fun getPayment(paymentId: Long): Mono<Payment> {
        return getRequest(GET_PAYMENT_DETAILS, mapOf("paymentId" to paymentId))
    }

}

data class Payment(
    @JsonProperty("is_refundable") val isRefundable: Boolean,
)