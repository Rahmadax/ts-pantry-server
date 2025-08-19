
package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.client.AddressClient
import com.depop.cx.drc.workflow.getLongProcessVariableOrNull
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.spin.Spin.JSON

private const val SELLER_ID_PROPERTY = "seller_id"
private const val SELLER_ADDRESS_ID_PROPERTY = "seller_address"
private const val SELLER_ADDRESS_OBJECT_PROPERTY = "seller_address_object"

class GetSellerAddressDrcDelegate(
    private val addressClient: AddressClient,
    taskMetrics: TaskMetrics
) : AbstractDrcDelegate(taskMetrics) {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val sellerAddressId = execution.getLongVariableOrNull(SELLER_ADDRESS_ID_PROPERTY)

        // Only process if sellerAddressId was provided and is not 0
        if (sellerAddressId != null && sellerAddressId != 0L) {
            val sellerId = execution.getLongProcessVariableOrNull(SELLER_ID_PROPERTY)
                ?: throwError("Seller ID not found or invalid for execution ID: ${execution.id}")

            val sellerAddresses = addressClient.getUserAddresses(sellerId).block()
                ?: throwError("Error fetching seller addresses for execution ID: ${execution.id}")

            val thisAddress = sellerAddresses.find { it.id == sellerAddressId }
                ?: throwError("Address with ID $sellerAddressId for sellerId $sellerId not found in response for execution ID: ${execution.id}")

            execution.setVariableLocal(SELLER_ADDRESS_OBJECT_PROPERTY, JSON(thisAddress))
        }
    }

    private fun throwError(message: String): Nothing {
        logger.warn { message }
        throw RuntimeException(message)
    }
}
