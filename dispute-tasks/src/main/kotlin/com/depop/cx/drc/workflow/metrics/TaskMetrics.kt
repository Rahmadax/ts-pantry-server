package com.depop.cx.drc.workflow.metrics

import com.depop.cx.drc.workflow.DelegateFailureMetadata
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag

object TaskMetricNames {
    const val DELEGATE_FAILURE = "camunda.delegate.failure"
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

    fun getDelegateFailureCounter(failureMetadata: DelegateFailureMetadata): Counter {
        return meterRegistry.counter(
            TaskMetricNames.DELEGATE_FAILURE, listOf(
                Tag.of(TaskMetricTags.PROCESS_DEFINITION_KEY, failureMetadata.processDefinitionKey ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.PROCESS_DEFINITION_VERSION, failureMetadata.processDefinitionVersion ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.ACTIVITY_ID, failureMetadata.activityId ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.ACTIVITY_NAME, failureMetadata.activityName ?: UNKNOWN_TAG_VALUE),
                Tag.of(TaskMetricTags.DELEGATE_CLASS_NAME, failureMetadata.delegateClassName),
                Tag.of(TaskMetricTags.EXCEPTION_CLASS_NAME, failureMetadata.exceptionClassName)
            )
        )
    }

}