package com.depop.cx.drc.workflow

import com.depop.cx.drc.workflow.metrics.TaskMetrics
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class DelegateFailureMetadataTest {

    @Test
    fun `from should build the expected DelegateFailureMetadata`() {
        val mockExecution = mock<DelegateExecution>()

        val processDefinitionId = "Process_0oxrtl6:1:9eb9c9f0-3f98-11f0-88e8-62dcf9e7fde"
        val activityId = "Activity_0oolzme"

        `when`(mockExecution.processDefinitionId).thenReturn(processDefinitionId)
        `when`(mockExecution.currentActivityId).thenReturn(activityId)
        `when`(mockExecution.currentActivityName).thenReturn("Foo Service Task")

        val exception = IllegalStateException("Banana")
        val delegate = TestDelegate(mock<TaskMetrics>())
        
        val metadata = DelegateFailureMetadata.from(mockExecution, exception, delegate::class.java)

        assertEquals(processDefinitionId, metadata.processDefinitionId)
        assertEquals("1", metadata.processDefinitionVersion)
        assertEquals(activityId, metadata.activityId)
        assertEquals("foo_service_task", metadata.activityName)
        assertEquals("TestDelegate", metadata.delegateClassName)
        assertEquals("IllegalStateException", metadata.exceptionClassName)
    }

    @Test
    fun `sanitiseActivityName lowercases and replaces spaces`() {
        val result = DelegateFailureMetadata.sanitiseActivityName("Send Confirmation Email")
        assertEquals("send_confirmation_email", result)
    }

    @Test
    fun `sanitiseActivityName removes non-alphanumeric characters`() {
        val result = DelegateFailureMetadata.sanitiseActivityName("Approve? Application!!")
        assertEquals("approve_application", result)
    }

    @Test
    fun `sanitiseActivityName trims surrounding whitespace and sanitises`() {
        val result = DelegateFailureMetadata.sanitiseActivityName("   Foo-Bar!  ")
        assertEquals("foobar", result)
    }

    @Test
    fun `sanitiseActivityName allows underscores`() {
        val result = DelegateFailureMetadata.sanitiseActivityName("Some_Task_Name")
        assertEquals("some_task_name", result)
    }

    @Test
    fun `sanitiseActivityName returns null if nothing remains after sanitising`() {
        val result = DelegateFailureMetadata.sanitiseActivityName("   !!!   ")
        assertNull(result)
    }

    @Test
    fun `sanitiseActivityName collapses multiple spaces`() {
        val result = DelegateFailureMetadata.sanitiseActivityName("Task   Name")
        assertEquals("task_name", result)
    }

    class TestDelegate(taskMetrics: TaskMetrics) : AbstractMetricDrcDelegate(taskMetrics) {
        override fun doExecute(execution: DelegateExecution) {
            TODO("Not yet implemented")
        }
    }
}