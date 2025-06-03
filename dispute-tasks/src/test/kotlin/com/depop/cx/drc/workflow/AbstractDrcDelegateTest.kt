package com.depop.cx.drc.workflow

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AbstractDrcDelegateTest {

    @Test
    fun `should record failure metric and timer when delegate throws`() {
        val registry = SimpleMeterRegistry()
        val taskMetrics = TaskMetrics(registry)

        val processDefinitionId = "Process_abc:1:def"
        val processDefinitionKey = "Process_abc"
        val processDefinitionVersion = "1"
        val activityId = "Activity_xyz"

        val execution = mock<DelegateExecution>()
        `when`(execution.processDefinitionId).thenReturn(processDefinitionId)
        `when`(execution.currentActivityId).thenReturn(activityId)
        `when`(execution.currentActivityName).thenReturn("My Task")

        val delegate = AlwaysThrowsDelegate(taskMetrics)

        try {
            delegate.execute(execution)
        } catch (_: Throwable) {
            //expected!
        }

        val counter =
            registry
                .get("camunda.delegate.failure")
                .tag(
                    "process_definition_key",
                    processDefinitionKey
                )
                .tag(
                    "process_definition_version",
                    processDefinitionVersion
                )
                .tag(
                    "activity_id",
                    activityId
                )
                .tag(
                    "activity_name",
                    "my_task"
                )
                .tag(
                    "exception_class_name",
                    "IllegalStateException"
                )
                .tag(
                    "delegate_class_name",
                    "AlwaysThrowsDelegate"
                )
                .counter()

        assertEquals(1.0, counter.count())

        val timer =
            registry
                .get("camunda.delegate.execution")
                .tag(
                    "process_definition_key",
                    processDefinitionKey
                )
                .tag(
                    "process_definition_version",
                    processDefinitionVersion
                )
                .tag(
                    "activity_id",
                    activityId
                )
                .tag(
                    "activity_name",
                    "my_task"
                )
                .tag(
                    "delegate_class_name",
                    "AlwaysThrowsDelegate"
                )
                .timer()

        assertEquals(1, timer.count())
    }

    @Test
    fun `should record timer only when delegate succeeds`() {
        val registry = SimpleMeterRegistry()
        val taskMetrics = TaskMetrics(registry)

        val processDefinitionId = "Process_abc:1:def"
        val processDefinitionKey = "Process_abc"
        val processDefinitionVersion = "1"
        val activityId = "Activity_xyz"

        val execution = mock<DelegateExecution>()
        `when`(execution.processDefinitionId).thenReturn(processDefinitionId)
        `when`(execution.currentActivityId).thenReturn(activityId)
        `when`(execution.currentActivityName).thenReturn("My Task")

        val delegate = AlwaysSucceedsDelegate(taskMetrics)
        delegate.execute(execution)

        assertEquals(1, registry.meters.size)

        val timer =
            registry
                .get("camunda.delegate.execution")
                .tag(
                    "process_definition_key",
                    processDefinitionKey
                )
                .tag(
                    "process_definition_version",
                    processDefinitionVersion
                )
                .tag(
                    "activity_id",
                    activityId
                )
                .tag(
                    "activity_name",
                    "my_task"
                )
                .tag(
                    "delegate_class_name",
                    "AlwaysSucceedsDelegate"
                )
                .timer()

        assertEquals(1, timer.count())
    }

    @Test
    fun `should record failure metric when delegate throws and execution is empty`() {
        val registry = SimpleMeterRegistry()
        val taskMetrics = TaskMetrics(registry)

        val delegate = AlwaysThrowsDelegate(taskMetrics)

        val execution = mock<DelegateExecution>()
        `when`(execution.processDefinitionId).thenReturn(null)
        `when`(execution.currentActivityId).thenReturn(null)
        `when`(execution.currentActivityName).thenReturn(null)

        try {
            delegate.execute(execution)
        } catch (_: Throwable) {
            //expected!
        }

        val counter =
            registry
                .get("camunda.delegate.failure")
                .tag(
                    "process_definition_key",
                    "unknown"
                )
                .tag(
                    "process_definition_version",
                    "unknown"
                )
                .tag(
                    "activity_id",
                    "unknown"
                )
                .tag(
                    "activity_name",
                    "unknown"
                )
                .tag(
                    "exception_class_name",
                    "IllegalStateException"
                )
                .tag(
                    "delegate_class_name",
                    "AlwaysThrowsDelegate"
                )
                .counter()

        assertEquals(1.0, counter.count())
    }

    class AlwaysThrowsDelegate(private val taskMetrics: TaskMetrics) : AbstractDrcDelegate(taskMetrics) {

        override fun doExecute(execution: DelegateExecution) {
            throw IllegalStateException("Banana")
        }

    }

    class AlwaysSucceedsDelegate(private val taskMetrics: TaskMetrics) : AbstractDrcDelegate(taskMetrics) {

        override fun doExecute(execution: DelegateExecution) {
        }

    }
}