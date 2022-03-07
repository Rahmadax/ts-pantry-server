package com.depop.cx.drc.workflow.listener

import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.camunda.spin.Spin.JSON

private const val CONTEXT_VARIABLE = "context"
private const val CONTEXT_STRING_VARIABLE = "context_string"
private const val ACTIVE_USER_VARIABLE = "active_user"
private const val INITIATOR_VARIABLE = "initiator"

class DefaultProcessStartListener : JavaDelegate {

    override fun execute(execution: DelegateExecution?) {
        if (ExecutionListener.EVENTNAME_START == execution?.eventName) {

            // If there is no JSON context but there is a String one, try and parse the String JSON,
            // else report the problem to the process.
            val context = execution.processInstance?.getVariable(CONTEXT_VARIABLE)
            val contextString = execution.processInstance?.getVariable(CONTEXT_STRING_VARIABLE) as String?
            if (context == null && contextString != null && contextString.isNotBlank()) {
                execution.setVariable("context", JSON(contextString))
            } else if (context == null) {
                throw BpmnError("No start context supplied. Please start this process with either a JSON context (context) or String context (context_string).")
            }

            // Record the active user as the initiator
            val activeUser = execution.processInstance.getVariable(ACTIVE_USER_VARIABLE)
            execution.processInstance.setVariable(INITIATOR_VARIABLE, activeUser)

        }
    }

}