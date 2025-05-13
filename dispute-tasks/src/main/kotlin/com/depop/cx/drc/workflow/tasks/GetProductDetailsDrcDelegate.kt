package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.ProductClient
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.getUUIDVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val PRODUCT_ID = "product_id"
private const val IS_PRODUCT_BANNED = "is_product_banned"
private const val DISPUTE_ID_PROPERTY = "dispute_id"

class GetProductDetailsDrcDelegate(
    private val productClient: ProductClient
) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val disputeId = execution.getUUIDVariableOrNull(DISPUTE_ID_PROPERTY)
        val productId = execution.getLongVariableOrNull(PRODUCT_ID)

        if (productId != null) {
            val product = productClient.getProduct(productId)
                .block()

            if (product == null) {
                logger.error { "Unable to find product $productId on dispute $disputeId" }
                throw BpmnError(
                    TaskErrorCode.FAILURE.code,
                    "Could not find product $productId for dispute $disputeId"
                )
            } else {
                execution.setVariableLocal(IS_PRODUCT_BANNED, ProductStatus.fromValue(product.status) == ProductStatus.BANNED)
            }

        } else {
            logger.error { "Could not find product id for dispute $disputeId" }
            throw BpmnError(
                TaskErrorCode.FAILURE.code,
                "Could not find product id for dispute $disputeId"
            )
        }
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
}