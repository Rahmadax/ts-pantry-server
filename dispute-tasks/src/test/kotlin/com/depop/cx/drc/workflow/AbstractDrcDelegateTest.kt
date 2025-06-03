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
    fun `should record failure metric when delegate throws`() {
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

        val delegate = TestDelegate(taskMetrics)

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
                    "TestDelegate"
                )
                .counter()

        assertEquals(1.0, counter.count())
    }


    @Test
    fun `should record failure metric when delegate throws and execution is empty`() {
        val registry = SimpleMeterRegistry()
        val taskMetrics = TaskMetrics(registry)

        val delegate = TestDelegate(taskMetrics)

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
                    "TestDelegate"
                )
                .counter()

        assertEquals(1.0, counter.count())
    }

    class TestDelegate(private val injectedMetrics: TaskMetrics) : AbstractDrcDelegate() {

        init {
            this.taskMetrics = injectedMetrics
        }

        override fun doExecute(execution: DelegateExecution) {
            throw IllegalStateException("Banana")
        }

    }
}