package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.spin.json.SpinJsonNode

private const val CONTEXT_VARIABLE = "context"

class UpdateContextDrcDelegate(private val taskMetrics: TaskMetrics) : AbstractDrcDelegate(taskMetrics) {

    override fun doExecute(execution: DelegateExecution) {
        val context = execution.processInstance.getVariable(CONTEXT_VARIABLE)
        if (context is SpinJsonNode) {
            execution.variablesLocal.entries.forEach {
                when (it.value) {
                    is Boolean -> context.prop(it.key, it.value as Boolean)
                    is Number -> context.prop(it.key, it.value as Number)
                    is String -> context.prop(it.key, it.value as String)
                    is SpinJsonNode -> context.prop(it.key, it.value as SpinJsonNode)
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
