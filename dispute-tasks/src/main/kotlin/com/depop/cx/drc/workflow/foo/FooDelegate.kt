package com.depop.cx.drc.workflow.foo

import com.depop.cx.drc.workflow.AbstractMetricDrcDelegate
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import org.camunda.bpm.engine.delegate.DelegateExecution

class FooDelegate(taskMetrics: TaskMetrics): AbstractMetricDrcDelegate(taskMetrics) {
    override fun doExecute(execution: DelegateExecution) {
        throw IllegalStateException("Banana")
    }
}