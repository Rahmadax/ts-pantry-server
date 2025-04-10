package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.client.User
import com.depop.cx.drc.workflow.client.UserClient
import com.depop.cx.drc.workflow.getLongVariableOrNull
import com.depop.cx.drc.workflow.getUUIDVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

private const val DISPUTE_ID_PROPERTY = "dispute_id"
private const val USER_ID_PROPERTY = "user_id"
private const val USERNAME = "username"
private const val USER_BANNED_PROPERTY = "user_banned"

class GetUserDetailsDrcDelegate(private val userClient: UserClient) : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val disputeId = execution.getUUIDVariableOrNull(DISPUTE_ID_PROPERTY)
        val userId = execution.getLongVariableOrNull(USER_ID_PROPERTY)

        if (userId != null) {
            setUserDetailsVariables(execution, disputeId, userId)
        } else {
            logger.error { "Unable to get a property $USER_ID_PROPERTY on dispute $disputeId " }
        }
    }

    private fun setUserDetailsVariables(execution: DelegateExecution, disputeId: UUID?, userId: Long) {
        logger.debug { "Getting user info for the user $userId on dispute $disputeId" }
        val user = userClient.getUser(userId).block()

        if (user != null) {
            updateContext(execution, user)
        } else {
            logger.error { "Unable to find user $userId on dispute $disputeId " }
        }
    }

    private fun updateContext(execution: DelegateExecution, user: User) {
        logger.info { "Setting username for the user ${user.id} to ${user.username}" }
        execution.processInstance.setVariableLocal(USERNAME, user.username)
        logger.info { "Setting banned status for the user ${user.id} to ${user.isBanned()}" }
        execution.processInstance.setVariableLocal(USER_BANNED_PROPERTY, user.isBanned())
    }
}