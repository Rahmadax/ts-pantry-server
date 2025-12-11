package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val GET_RETURNABLE_ITEMS = "/internal/v1/purchases/{purchase_id}/return/options"
private const val CREATE_RETURN = "/internal/v1/purchases/{purchase_id}/return"
private const val CANCEL_RETURN = "/internal/v1/returns/{return_id}/cancel"

class ReturnsClient(webClient: WebClient) : Client(webClient) {

    fun getReturnableItems(
        purchaseId: Long,
        buyerAddressId: Long,
        sellerAddressId: Long
    ): Mono<ReturnableItemsResponse> {
        return getRequest(
            "$GET_RETURNABLE_ITEMS?buyer_address_id={buyerAddressId}&seller_address_id={sellerAddressId}",
            mapOf(
                "purchase_id" to purchaseId,
                "buyerAddressId" to buyerAddressId,
                "sellerAddressId" to sellerAddressId
            )
        )
    }

    fun createReturn(
        purchaseId: Long,
        request: CreateReturnRequest
    ): Mono<CreateReturnResponse> {
        return postRequest(CREATE_RETURN, request, mapOf("purchase_id" to purchaseId))
    }

    fun cancelReturn(returnId: String): Mono<CancelReturnResponse> {
        return postRequest(CANCEL_RETURN, EmptyRequest(), mapOf("return_id" to returnId))
    }
}

data class ReturnableItemsResponse(
    @JsonProperty("purchase_items") val purchaseItems: Map<String, ReturnMethods>
)

data class ReturnMethods(
    @JsonProperty("return_methods") val returnMethods: List<String>
)

data class CreateReturnRequest(
    @JsonProperty("purchase_item_ids") val purchaseItemIds: List<String>,
    @JsonProperty("buyer_address_id") val buyerAddressId: String,
    @JsonProperty("seller_address_id") val sellerAddressId: String,
    @JsonProperty("return_method") val returnMethod: String
)

data class CreateReturnResponse(
    @JsonProperty("return_id") val returnId: String
)

class EmptyRequest

data class CancelReturnResponse(
    val placeholder: String? = null
)
