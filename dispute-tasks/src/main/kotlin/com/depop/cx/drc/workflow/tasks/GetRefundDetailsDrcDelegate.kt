package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.client.CheckoutClient
import com.depop.cx.drc.workflow.client.RefundDetails
import com.depop.cx.drc.workflow.getLongVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

private const val RECEIPT_ID_PROPERTY = "receipt_id"
private const val BUYER_REFUND_AMOUNT = "buyer_refund_amount"
private const val SELLER_REFUND_AMOUNT = "seller_refund_amount"
private const val REFUND_CURRENCY = "refund_currency"

class GetRefundDetailsDrcDelegate(
    private val checkoutClient: CheckoutClient
) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val receiptId = execution.getLongVariableOrNull(RECEIPT_ID_PROPERTY)

        val maybeRefundDetails = receiptId?.let { getReceiptDetails(it) }
            ?: run {
                logger.warn { "Receipt Id not found for execution ID: ${execution.id}" }
                null
            }

        setVariables(execution, maybeRefundDetails)
    }


    private fun getReceiptDetails(receiptId: Long): RefundDetails? {
        try {
            val receipt = checkoutClient.getReceipt(receiptId)
                .block()

            if (receipt == null) {
                logger.warn {"Receipt not found for id: $receiptId" }
                return null
            }

            if (receipt.refundDetails == null) {
                logger.warn {"Receipt found but refundDetails is null for receipt id: $receiptId"}
                return null
            }

            return receipt.refundDetails
        } catch (e: Exception) {
            logger.warn {"Error retrieving receipt with id: $receiptId. Error: $e. Recovering with null refund details" }
            return null
        }
    }


    private fun setVariables(execution: DelegateExecution, refund: RefundDetails?) {
        execution.setVariable(BUYER_REFUND_AMOUNT, refund?.buyerRefundAmount?.toString() ?: "")
        execution.setVariable(SELLER_REFUND_AMOUNT, refund?.sellerRefundAmount?.toString() ?: "")
        execution.setVariable(REFUND_CURRENCY, refund?.refundCurrency?.let { getSymbol(it) } ?: "")
    }

    fun getSymbol(currencyCode: String): String {
        return try {
            Currency.getInstance(currencyCode).symbol

        } catch (e: IllegalArgumentException) {
            logger.warn { "Recovering from invalid currency code: $currencyCode. Message: ${e.message}" }
            return "$currencyCode "
        }
    }
}