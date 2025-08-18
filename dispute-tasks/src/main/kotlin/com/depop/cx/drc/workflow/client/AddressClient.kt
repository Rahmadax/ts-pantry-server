package com.depop.cx.drc.workflow.client


import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val GET_ADDRESS = "/internal/v1/addresses/{address_id}"
private const val GET_USER_ADDRESSES = "/internal/v1/addresses/user/{user_id}"

class AddressClient(webClient: WebClient) : Client(webClient) {

    fun getUserAddresses(sellerId: Long): Mono<List<Address>> {
        return getRequest(GET_USER_ADDRESSES, mapOf("user_id" to sellerId))
    }

    fun getAddress(addressId: Long): Mono<Address> {
        return getRequest(GET_ADDRESS, mapOf("address_id" to addressId))
    }
}

data class Address (
    @JsonProperty("id") val id: Long?,
    @JsonProperty("name") val name: String,
    @JsonProperty("address") val address: String,
    @JsonProperty("address2") val address2: String?,
    @JsonProperty("city") val city: String,
    @JsonProperty("state") val state: String,
    @JsonProperty("postal_code") val postalCode: String,
    @JsonProperty("country") val country: String
)