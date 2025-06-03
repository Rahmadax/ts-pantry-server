package com.depop.cx.drc.workflow.metrics

import com.depop.cx.drc.workflow.DelegateMetadata
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer

object TaskMetricNames {
    const val DELEGATE_FAILURE = "camunda.delegate.failure"
    const val DELEGATE_EXECUTION = "camunda.delegate.execution"
}

object TaskMetricTags {
    const val PROCESS_DEFINITION_KEY = "process_definition_key"
    const val PROCESS_DEFINITION_VERSION = "process_definition_version"
    const val ACTIVITY_ID = "activity_id"
    const val ACTIVITY_NAME = "activity_name"
    const val DELEGATE_CLASS_NAME = "delegate_class_name"
    const val EXCEPTION_CLASS_NAME = "exception_class_name"
}

const val UNKNOWN_TAG_VALUE = "unknown"

class TaskMetrics(private val meterRegistry: MeterRegistry) {

    fun getDelegateFailureCounter(delegateMetadata: DelegateMetadata, exceptionClass: Class<*>): Counter {
        return meterRegistry.counter(
            TaskMetricNames.DELEGATE_FAILURE, listOf(
                Tag.of(
                    TaskMetricTags.PROCESS_DEFINITION_KEY,
                    delegateMetadata.processDefinitionKey ?: UNKNOWN_TAG_VALUE
                ),
                Tag.of(
                    TaskMetricTags.PROCESS_DEFINITION_VERSION,
                    delegateMetadata.processDefinitionVersion ?: UNKNOWN_TAG_VALUE
                ),
                Tag.of(TaskMetricTags.ACTIVITY_ID, delegateMetadata.activityId ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.ACTIVITY_NAME, delegateMetadata.activityName ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.DELEGATE_CLASS_NAME, delegateMetadata.delegateClassName),
                Tag.of(TaskMetricTags.EXCEPTION_CLASS_NAME, exceptionClass.simpleName)
            )
        )
    }

    fun getDelegateExecutionTimer(delegateMetadata: DelegateMetadata): Timer {
        return meterRegistry.timer(
            TaskMetricNames.DELEGATE_EXECUTION, listOf(
                Tag.of(
                    TaskMetricTags.PROCESS_DEFINITION_KEY,
                    delegateMetadata.processDefinitionKey ?: UNKNOWN_TAG_VALUE
                ),
                Tag.of(
                    TaskMetricTags.PROCESS_DEFINITION_VERSION,
                    delegateMetadata.processDefinitionVersion ?: UNKNOWN_TAG_VALUE
                ),
                Tag.of(TaskMetricTags.ACTIVITY_ID, delegateMetadata.activityId ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.ACTIVITY_NAME, delegateMetadata.activityName ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.DELEGATE_CLASS_NAME, delegateMetadata.delegateClassName)
            )
        )
    }

}