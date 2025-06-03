package com.depop.cx.drc.workflow

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class DelegateFailureMetadataTest {

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

    @Test
    fun `extractProcessDefinitionKey on empty string`() {
        val result = DelegateFailureMetadata.extractProcessDefinitionKey("")
        assertEquals("", result)
    }

    @Test
    fun `extractProcessDefinitionVersion on empty string`() {
        val result = DelegateFailureMetadata.extractProcessDefinitionVersion("")
        assertNull(result)
    }

    @Test
    fun `extractProcessDefinitionVersion on blank`() {
        val result = DelegateFailureMetadata.extractProcessDefinitionVersion("foo:")
        assertNull(result)
    }

    @Test
    fun `extractProcessDefinitionVersion on non-integer`() {
        val result = DelegateFailureMetadata.extractProcessDefinitionVersion("foo:bar")
        assertNull(result)
    }

}