package com.depop.cx.drc.workflow

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DelegateFailureMetadataTest {

    @Test
    fun `sanitiseActivityName lowercases and replaces spaces`() {
        val result = DelegateMetadata.sanitiseActivityName("Send Confirmation Email")
        assertEquals("send_confirmation_email", result)
    }

    @Test
    fun `sanitiseActivityName removes non-alphanumeric characters`() {
        val result = DelegateMetadata.sanitiseActivityName("Approve? Application!!")
        assertEquals("approve_application", result)
    }

    @Test
    fun `sanitiseActivityName trims surrounding whitespace and sanitises`() {
        val result = DelegateMetadata.sanitiseActivityName("   Foo-Bar!  ")
        assertEquals("foobar", result)
    }

    @Test
    fun `sanitiseActivityName allows underscores`() {
        val result = DelegateMetadata.sanitiseActivityName("Some_Task_Name")
        assertEquals("some_task_name", result)
    }

    @Test
    fun `sanitiseActivityName returns null if nothing remains after sanitising`() {
        val result = DelegateMetadata.sanitiseActivityName("   !!!   ")
        assertNull(result)
    }

    @Test
    fun `sanitiseActivityName collapses multiple spaces`() {
        val result = DelegateMetadata.sanitiseActivityName("Task   Name")
        assertEquals("task_name", result)
    }

    @Test
    fun `extractProcessDefinitionKey on empty string`() {
        val result = DelegateMetadata.extractProcessDefinitionKey("")
        assertNull(result)
    }

    @Test
    fun`extractProcessDefinitionKey on uuid`() {
        // camunda has been known to change the process definition id to just be a UUID, if it gets too long!
        // we should avoid cardinality explosion by not extracting a process key if it has done that
        val result = DelegateMetadata.extractProcessDefinitionKey("a253e423-b103-48bc-9f22-c98cb7d714f2")
        assertNull(result)
    }

    @Test
    fun `extractProcessDefinitionVersion on empty string`() {
        val result = DelegateMetadata.extractProcessDefinitionVersion("")
        assertNull(result)
    }

    @Test
    fun `extractProcessDefinitionVersion on blank`() {
        val result = DelegateMetadata.extractProcessDefinitionVersion("foo:")
        assertNull(result)
    }

    @Test
    fun `extractProcessDefinitionVersion on non-integer`() {
        val result = DelegateMetadata.extractProcessDefinitionVersion("foo:bar")
        assertNull(result)
    }

}