package com.depop.cx.drc.workflow.client

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

private const val GET_SHIPPING_DETAILS = "/internal/v2/shipping-statuses/{receiptId}/"
private const val GET_PARCEL_IDS = "/internal/v2/parcels/by-purchase-id/{receiptId}/"
private const val GET_PARCEL_DETAILS = "/internal/v3/parcels/batch/{parcelIds}/"


class ShippingClient(webClient: WebClient) : Client(webClient) {

    fun getShippingStatus(receiptId: Long): Mono<Map<String, String>> {
        return getRequest(GET_SHIPPING_DETAILS, mapOf("receiptId" to receiptId))
    }

    fun getParcelIds(receiptId: Long): Mono<ParcelIds> {
        return getRequest(GET_PARCEL_IDS, mapOf("receiptId" to receiptId))
    }

    fun getParcelDetails(parcelIds: List<String>): Mono<Parcels> {
        return getRequest(GET_PARCEL_DETAILS, mapOf("parcelIds" to parcelIds.joinToString(",")))
    }

}

data class ParcelIds(@JsonProperty("parcels") val ids: List<String>)

class Parcels : HashMap<String, ParcelDetails>()

data class ParcelDetails(
    @JsonProperty("parcel_id") val id: String,
    @JsonProperty("provider_specific_details") val providerDetails: ParcelProviderDetails?
)

data class ParcelProviderDetails(
    @JsonProperty("state") val status: String?,
    @JsonProperty("shipping_tracking_number") val manualParcelTrackingNumber: String?,
    @JsonProperty("tracking") val depopParcelTracking: DepopParcelTracking?
)

data class DepopParcelTracking(@JsonProperty("reference") val reference: String?)