package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val GET_PRODUCT_DETAILS = "/api/v2/products/{id}/?all=true"

class ProductClient(webClient: WebClient) : Client(webClient) {

    fun getProduct(productId: Long): Mono<Product> {
        return getRequest(GET_PRODUCT_DETAILS, mapOf("id" to productId))
    }
}

data class Product(
    @JsonProperty("id") val id: Long,
    @JsonProperty("status") val status: String,
)