package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.spin.json.SpinJsonNode

private const val CONTEXT_VARIABLE = "context"

class UpdateContextDrcDelegate : AbstractDrcDelegate() {

    override fun doExecute(execution: DelegateExecution) {
        val context = execution.processInstance.getVariable(CONTEXT_VARIABLE)
        if (context is SpinJsonNode) {
            execution.variablesLocal.entries.forEach {
                when (it.value) {
                    is Boolean -> context.prop(it.key, it.value as Boolean)
                    is Number -> context.prop(it.key, it.value as Number)
                    is String -> context.prop(it.key, it.value as String)
                    is Long -> context.prop(it.key, it.value as Long)
                    is Int -> context.prop(it.key, it.value as Int)
                    is Float -> context.prop(it.key, it.value as Float)
                    else -> if (context.hasProp(it.key)) {
                        context.remove(it.key)
                    }
                }
            }
            execution.processInstance.setVariable(CONTEXT_VARIABLE, context)
        }
    }
}
