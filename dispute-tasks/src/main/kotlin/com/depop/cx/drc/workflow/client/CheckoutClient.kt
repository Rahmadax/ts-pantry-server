package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.time.ZonedDateTime

private const val GET_RECEIPT_DETAILS = "/internal/v2/receipts/{receiptId}/"

class CheckoutClient(webClient: WebClient) : Client(webClient) {

    fun getReceipt(receiptId: Long): Mono<Receipt> {
        return getRequest(GET_RECEIPT_DETAILS, mapOf("receiptId" to receiptId))
    }

}

data class Receipt(
    @JsonProperty("purchase_internal_id") val id: Long,
    @JsonProperty("buyer_id") val buyerId: Long,
    @JsonProperty("seller_id") val sellerId: Long,
    @JsonProperty("created") val created: ZonedDateTime,
    @JsonProperty("payment_provider") val paymentProvider: String,
    @JsonProperty("payment_id") val paymentId: Long,
    @JsonProperty("buyer_amount") val buyerAmount: BigDecimal,
    @JsonProperty("refund_details") val refundDetails: RefundDetails?
) {
    fun isFullyRefunded(): Boolean {
        return refundDetails?.buyerRefundAmount?.compareTo(buyerAmount) == 0
    }
}

data class RefundDetails(
    @JsonProperty("buyer_refund_amount") val buyerRefundAmount: BigDecimal?
)