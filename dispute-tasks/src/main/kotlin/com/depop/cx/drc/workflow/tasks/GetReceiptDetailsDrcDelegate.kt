package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.*
import com.depop.cx.drc.workflow.getLongVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import reactor.core.publisher.Mono
import java.time.ZonedDateTime

private const val RECEIPT_ID_PROPERTY = "receipt_id"
private const val BUYER_ID_PROPERTY = "buyer"
private const val SELLER_ID_PROPERTY = "seller"
private const val RECEIPT_CREATED_AT_PROPERTY = "receipt_created_at"
private const val PAYMENT_PROVIDER_PROPERTY = "payment_provider"
private const val SHIPPING_STATUS_PROPERTY = "shipping_status"
private const val IS_REFUNDABLE_PROPERTY = "is_refundable"
private const val IS_TRACKED_PROPERTY = "is_tracked"
private const val PARCEL_ID = "parcel_id"
private const val IS_FULLY_REFUNDED = "is_fully_refunded"
private const val SHIPPED_TIME = "shipped_at"

data class ReceiptDetails(
    val receipt: Receipt,
    val shippingStatus: String,
    val isRefundable: Boolean,
    val isTracked: Boolean,
    val parcelId: String?,
    val shippedAt: ZonedDateTime?
)

data class PrimaryParcelDetails(
    val isTracked: Boolean,
    val parcelId: String?,
    val shippedAt: ZonedDateTime?
)

class GetReceiptDetailsDrcDelegate(
    private val checkoutClient: CheckoutClient,
    private val paymentsClient: PaymentsClient,
    private val shippingClient: ShippingClient
) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {

        setVariables(execution, null)

        val receiptId = execution.getLongVariableOrNull(RECEIPT_ID_PROPERTY)
            ?: throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "$RECEIPT_ID_PROPERTY must not be null."
            )

        logger.info { "Getting receipt details for receipt $receiptId" }
        val details = getReceiptDetails(receiptId)
        setVariables(execution, details)

    }

    private fun getReceiptDetails(receiptId: Long): ReceiptDetails {
        return Mono.zip(
            getReceipt(receiptId).zipWhen { receipt -> isRefundable(receipt) },
            getShippingStatus(receiptId),
            getPrimaryParcelDetails(receiptId)
        ).flatMap { data ->
            val receipt = data.t1.t1
            val isRefundable = data.t1.t2
            val shippingStatus = data.t2
            val parcel = data.t3
            Mono.just(
                ReceiptDetails(
                    receipt,
                    shippingStatus,
                    isRefundable,
                    parcel.isTracked,
                    parcel.parcelId,
                    parcel.shippedAt
                )
            )
        }.block()
            ?: throw IllegalStateException("Unable to find receipt with id $receiptId")
    }

    private fun getReceipt(receiptId: Long): Mono<Receipt> {
        return checkoutClient.getReceipt(receiptId)
    }

    private fun getShippingStatus(receiptId: Long): Mono<String> {
        return shippingClient.getShippingStatus(receiptId)
            .mapNotNull { status -> status?.getOrDefault("$receiptId", "UNKNOWN") ?: "UNKNOWN" }
    }

    private fun isRefundable(receipt: Receipt): Mono<Boolean> {
        return if ("STRIPE" != receipt.paymentProvider && "KLARNA" != receipt.paymentProvider) Mono.just(false)
        else paymentsClient.getPayment(receipt.paymentId).mapNotNull { payment -> payment?.isRefundable ?: false }
    }

    private fun getPrimaryParcelDetails(receiptId: Long): Mono<PrimaryParcelDetails> {
        return shippingClient.getParcelIds(receiptId)
            .flatMap { parcels -> shippingClient.getParcelDetails(parcels.ids) }
            .map {
                if (it == null || it.isEmpty()) {
                    PrimaryParcelDetails(false, null, null)
                } else {

                    // There are potentially multiple parcels, for the time being we'll use the first.
                    // This needs improvement as order is not defined.
                    val parcel = it[it.keys.first()]
                    val trackingNumber = getTrackingNumber(parcel)
                    val isTracked = trackingNumber?.isNotBlank() ?: false

                    // The parcel ID is used
                    val parcelId = parcel?.id
                    val shippedAt = getShippedTime(parcel)
                    PrimaryParcelDetails(isTracked, parcelId, shippedAt)
                }
            }
    }

    private fun getTrackingNumber(parcel: ParcelDetails?): String? {
        val trackingNumber: String?
        // Parcels with a depop shipping label should always have a tracking number.  Manually shipped parcels
        // may not have one.
        if (parcel?.providerDetails?.depopParcelTracking != null) {
            trackingNumber = parcel.providerDetails.depopParcelTracking.reference ?: "Currently unavailable"
        } else {
            trackingNumber = parcel?.providerDetails?.manualParcelTrackingNumber
        }
        return trackingNumber
    }

    private fun getShippedTime(parcel: ParcelDetails?): ZonedDateTime? {
        return parcel?.providerDetails?.depopParcelShippedAt ?: parcel?.providerDetails?.manualParcelShippedAt
    }

    private fun setVariables(execution: DelegateExecution, details: ReceiptDetails?) {
        execution.setVariableLocal(BUYER_ID_PROPERTY, details?.receipt?.buyerId?.toString() ?: "")
        execution.setVariableLocal(SELLER_ID_PROPERTY, details?.receipt?.sellerId?.toString() ?: "")
        execution.setVariableLocal(
            RECEIPT_CREATED_AT_PROPERTY,
            details?.receipt?.created?.toOffsetDateTime()?.toString() ?: ""
        ) // Using offset to ensure ISO8601 compatibility
        execution.setVariableLocal(PAYMENT_PROVIDER_PROPERTY, details?.receipt?.paymentProvider ?: "")
        execution.setVariableLocal(SHIPPING_STATUS_PROPERTY, details?.shippingStatus ?: "")
        execution.setVariableLocal(IS_REFUNDABLE_PROPERTY, details?.isRefundable ?: "")
        execution.setVariableLocal(IS_TRACKED_PROPERTY, details?.isTracked ?: "")
        execution.setVariableLocal(PARCEL_ID, details?.parcelId ?: "")
        execution.setVariableLocal(IS_FULLY_REFUNDED, details?.receipt?.isFullyRefunded() ?: "")
        execution.setVariableLocal(SHIPPED_TIME, details?.shippedAt?.toOffsetDateTime()?.toString() ?: "")
    }
}