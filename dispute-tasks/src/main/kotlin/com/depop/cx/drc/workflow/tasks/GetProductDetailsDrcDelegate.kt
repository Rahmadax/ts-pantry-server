package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.CheckoutClient
import com.depop.cx.drc.workflow.client.ProductClient
import com.depop.cx.drc.workflow.client.Receipt
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.getUUIDVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import kotlin.math.log

private const val RECEIPT_ID = "receipt_id"
private const val PRODUCT_ID = "product_id"
private const val IS_PRODUCT_BANNED = "is_product_banned"
private const val DISPUTE_ID_PROPERTY = "dispute_id"

class GetProductDetailsDrcDelegate(
    private val productClient: ProductClient,
    private val checkoutClient: CheckoutClient
) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val disputeId = execution.getUUIDVariableOrNull(DISPUTE_ID_PROPERTY)
        val receiptId = execution.getLongVariableOrNull(RECEIPT_ID)
            ?: run {
                logger.error { "Could not find receiptId id for dispute $disputeId" }
                throw BpmnError(
                    TaskErrorCode.FAILURE.code,
                    "Could not find receiptId id for dispute $disputeId"
                )
            }

        val receipt = checkoutClient.getReceipt(receiptId)
            .block()
            ?: run {
                logger.error { "Could not find receipt: $receiptId for dispute $disputeId" }
                throw BpmnError(
                    TaskErrorCode.FAILURE.code,
                    "Could not find receipt: $receiptId for dispute $disputeId"
                )
            }

        val firstProductId = getFirstProductId(receipt, execution.id)
            ?: run {
                logger.error { "Receipt ${receipt.id} has no product lines for dispute $disputeId" }
                throw BpmnError(
                    TaskErrorCode.FAILURE.code,
                    "Receipt ${receipt.id} has no product lines for dispute $disputeId"
                )
            }

        val product = productClient.getProduct(firstProductId)
            .block()
            ?: run {
                logger.error { "Unable to find product $firstProductId on dispute $disputeId" }
                throw BpmnError(
                    TaskErrorCode.FAILURE.code,
                    "Could not find product $firstProductId for dispute $disputeId"
                )
            }

        execution.setVariableLocal(
            IS_PRODUCT_BANNED,
            ProductStatus.fromValue(product.status) == ProductStatus.BANNED
        )
        execution.setVariableLocal(PRODUCT_ID, firstProductId)
    }

    enum class ProductStatus {
        BANNED, OTHER;

        companion object {
            fun fromValue(value: String): ProductStatus {
                return when (value.uppercase()) {
                    "B" -> BANNED
                    else -> OTHER
                }
            }
        }
    }

    private fun getFirstProductId(receipt: Receipt, executionId: String): Long? {
        if (receipt.lines.size > 1) {
            logger.error { "Receipt ${receipt.id} has multiple product lines (${receipt.lines.size}). Cannot process for $executionId. Falling back to first item" }
        }

        return receipt.lines.firstOrNull()?.productId
    }
}