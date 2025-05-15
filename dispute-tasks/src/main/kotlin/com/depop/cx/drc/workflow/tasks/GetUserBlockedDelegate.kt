package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.TaskErrorCode
import com.depop.cx.drc.workflow.client.BlockingClient
import com.depop.cx.drc.workflow.getLongVariableOrNull
import kotlinx.coroutines.runBlocking
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution

private const val BUYER_ID_PROPERTY = "buyer"
private const val SELLER_ID_PROPERTY = "seller"
private const val IS_USER_BLOCKED_PROPERTY = "is_user_blocked"

class GetUserBlockedDelegate(private val client: BlockingClient) : AbstractDrcDelegate() {

    override fun doExecute(execution: DelegateExecution) {

        val sellerId = execution.getLongVariableOrNull(SELLER_ID_PROPERTY) ?: throw BpmnError(
            TaskErrorCode.FAILURE.code,
            "$SELLER_ID_PROPERTY must not be null."
        )

        val buyerId = execution.getLongVariableOrNull(BUYER_ID_PROPERTY) ?: throw BpmnError(
            TaskErrorCode.FAILURE.code,
            "$BUYER_ID_PROPERTY must not be null."
        )

        val userBlocked = runBlocking {
            val buyer = client.getBlockStatus(buyerId, sellerId)
            val seller = client.getBlockStatus(sellerId, buyerId)
            buyer.blocked || seller.blocked
        }

        execution.setVariableLocal(IS_USER_BLOCKED_PROPERTY, userBlocked)

    }


}