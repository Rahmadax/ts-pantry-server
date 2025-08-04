package com.depop.cx.drc.workflow.tasks

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.spin.json.SpinJsonNode

private const val CONTEXT_VARIABLE = "context"

class UpdateContextDrcDelegate(taskMetrics: TaskMetrics) : AbstractDrcDelegate(taskMetrics) {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val context = execution.processInstance.getVariable(CONTEXT_VARIABLE)
        if (context is SpinJsonNode) {
            execution.variablesLocal.entries.forEach {
                if(it.key != CONTEXT_VARIABLE) { // Don't add the context to itself
                    when (it.value) {
                        is Boolean -> context.prop(it.key, it.value as Boolean)
                        is Number -> context.prop(it.key, it.value as Number)
                        is String -> context.prop(it.key, it.value as String)
                        is SpinJsonNode -> context.prop(it.key, it.value as SpinJsonNode)
                        is Long -> context.prop(it.key, it.value as Long)
                        is Int -> context.prop(it.key, it.value as Int)
                        is Float -> context.prop(it.key, it.value as Float)
                        else -> if (it.value == null && context.hasProp(it.key)) {
                            logger.info { "Removing context property ${it.key} with value ${it.value} from process ${execution.processInstance.id}" }
                            try {
                                context.deleteProp(it.key)
                            } catch (e: Exception) {
                                logger.error(e) { "Failed to remove context property ${it.key} from process ${execution.processInstance.id}" }
                            }
                        }
                    }
                }
            }
            execution.processInstance.setVariable(CONTEXT_VARIABLE, context)
        }
    }

}