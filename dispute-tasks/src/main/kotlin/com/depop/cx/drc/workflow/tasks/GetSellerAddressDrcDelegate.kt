package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.AddressClient
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.spin.Spin.JSON

private const val SELLER_ADDRESS_ID_PROPERTY = "seller_address"
private const val SELLER_ADDRESS_OBJECT_PROPERTY = "seller_address_object"

class GetSellerAddressDrcDelegate(
    private val addressClient: AddressClient,
    private val taskMetrics: TaskMetrics
) : AbstractDrcDelegate(taskMetrics) {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val sellerAddressId = execution.getLongVariableOrNull(SELLER_ADDRESS_ID_PROPERTY)
            ?: throwBpmnError("Seller Address ID not found for execution ID: ${execution.id}")

        val sellerAddress = addressClient.getAddress(sellerAddressId).block()
            ?: throwBpmnError("No addresses found for address ID: $SELLER_ADDRESS_ID_PROPERTY, execution ID: ${execution.id}")

        execution.setVariableLocal(SELLER_ADDRESS_OBJECT_PROPERTY, JSON(sellerAddress))
    }

    private fun throwBpmnError(message: String): Nothing {
        logger.warn { message }
        throw BpmnError(TaskErrorCode.FAILURE.code, message)
    }
}