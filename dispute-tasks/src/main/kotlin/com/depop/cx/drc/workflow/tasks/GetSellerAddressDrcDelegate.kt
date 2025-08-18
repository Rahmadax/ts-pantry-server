
package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.client.AddressClient
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.spin.Spin.JSON

private const val SELLER_ID_PROPERTY = "seller_id"
private const val SELLER_ADDRESS_ID_PROPERTY = "seller_address"
private const val SELLER_ADDRESS_OBJECT_PROPERTY = "seller_address_object"

class GetSellerAddressDrcDelegate(
    private val addressClient: AddressClient,
    private val taskMetrics: TaskMetrics
) : AbstractDrcDelegate(taskMetrics) {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val sellerId = execution.getVariable(SELLER_ID_PROPERTY)
        val sellerAddressId = execution.getVariable(SELLER_ADDRESS_ID_PROPERTY)

        val sellerIdLong = (sellerId as? String)?.toLongOrNull()
            ?: throwBpmnError("Seller ID not found or invalid for execution ID: ${execution.id}")

        val sellerAddressIdLong = (sellerAddressId as? String)?.toLongOrNull()
            ?: throwBpmnError("Seller Address ID not found or invalid for execution ID: ${execution.id}")

        // Getting the seller's address list and searching for a provided addressId avoids letting users upload addresses that are not theirs
        val sellerAddresses = addressClient.getUserAddresses(sellerIdLong).block()
            ?: throwBpmnError("Error fetching seller addresses for execution ID: ${execution.id}")

        val thisAddress = sellerAddresses.find { it.id == sellerAddressIdLong }
            ?: throwBpmnError("Address with ID $sellerAddressIdLong for sellerId $sellerIdLong not found in response for execution ID: ${execution.id}")

        execution.setVariableLocal(SELLER_ADDRESS_OBJECT_PROPERTY, JSON(thisAddress))
    }

    private fun throwBpmnError(message: String): Nothing {
        logger.warn { message }
        throw RuntimeException(message)
    }
}
