package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.CheckoutClient
import com.depop.cx.drc.workflow.client.ProductClient
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.getUUIDVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val RECEIPT_ID = "receipt_id"
private const val PRODUCT_ID = "product_id"
private const val IS_PRODUCT_BANNED = "is_product_banned"
private const val IS_BUNDLE = "is_bundle"
private const val DISPUTE_ID_PROPERTY = "dispute_id"

class GetProductDetailsDrcDelegate(
    private val productClient: ProductClient,
    private val checkoutClient: CheckoutClient,
    taskMetrics: TaskMetrics
) : AbstractDrcDelegate(taskMetrics) {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val disputeId = execution.getUUIDVariableOrNull(DISPUTE_ID_PROPERTY)
            ?: logAndThrowError("Could not find disputeId for execution: ${execution.id}")

        val receiptId = execution.getLongVariableOrNull(RECEIPT_ID)
            ?: logAndThrowError("Could not find receiptId for dispute: $disputeId")

        val receipt = checkoutClient.getReceipt(receiptId)
            .block()
            ?: logAndThrowError("Could not find receipt: $receiptId for dispute: $disputeId")

        val firstProductId = receipt.lines.firstOrNull()?.productId
            ?: logAndThrowError("No product found on receipt: $receiptId for dispute: $disputeId")

        val product = productClient.getProduct(firstProductId)
            .block()
            ?: logAndThrowError("Unable to find product: $firstProductId on dispute: $disputeId")

        val isBundle = receipt.lines.size > 1
        if (isBundle) {
            logger.warn { "Receipt: $receiptId for dispute: $disputeId contains multiple products (${receipt.lines.size}). Falling back to first item" }
        }

        execution.setVariableLocal(PRODUCT_ID, firstProductId)
        execution.setVariableLocal(IS_PRODUCT_BANNED, ProductStatus.fromValue(product.status) == ProductStatus.BANNED)
        execution.setVariableLocal(IS_BUNDLE, isBundle)
    }

    fun logAndThrowError(text: String): Nothing {
        logger.error { text }
        throw BpmnError(
            TaskErrorCode.FAILURE.code,
            text
        )
    }

    enum class ProductStatus {
        BANNED, OTHER;

        companion object {
            fun fromValue(value: String): ProductStatus {
                return when (value.uppercase()) {
                    "B" -> BANNED
                    "C" -> BANNED
                    else -> OTHER
                }
            }
        }
    }

}