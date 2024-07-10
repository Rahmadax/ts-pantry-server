package com.depop.cx.drc.workflow.tasks

import org.camunda.bpm.engine.delegate.DelegateExecution
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

private const val ISO_CREATED_DATE_PROPERTY = "created_date"
private const val ISO_RESPONSE_DUE_DATE_PROPERTY = "response_due_date"
private const val ISO_RESPONDED_DATE_PROPERTY = "responded_date"
private const val ISO_EMBARGO_DATE_PROPERTY = "embargo_date"
private const val ISO_AUTO_ESCALATION_DATE_PROPERTY = "auto_escalation_date"
private const val ISO_ESCALATED_DATE_PROPERTY = "escalated_date"
private const val ISO_RESOLVED_DATE_PROPERTY = "resolved_date"

private const val RESPONSE_DUE_DAYS_PROPERTY = "response_due_days"
private const val EMBARGO_DAYS_PROPERTY = "embargo_days"
private const val AUTO_ESCALATION_DAYS_PROPERTY = "auto_escalation_days"

private const val EMBARGO_DAYS_DEFAULT = 5L
private const val AUTO_ESCALATION_DAYS_DEFAULT = 2L
private const val RESPONSE_DAYS_DEFAULT = 2L

class SetProcessDatesTask : AbstractTask() {

    override fun doExecute(execution: DelegateExecution) {

        val now = OffsetDateTime.ofInstant(
            Instant.now(),
            ZoneId.of("UTC")
        )

        // The date that the dispute was created, i.e. now.
        execution.setVariableLocal(ISO_CREATED_DATE_PROPERTY, now.toString())

        // The date of the last response.
        execution.setVariableLocal(ISO_RESPONDED_DATE_PROPERTY, now.toString())

        // The date the issue was resolved.
        execution.setVariableLocal(ISO_RESOLVED_DATE_PROPERTY, now.toString())

        // The date the issue was resolved.
        execution.setVariableLocal(ISO_ESCALATED_DATE_PROPERTY, now.toString())

        // The date on which the user can open a dispute.
        var embargoDays = EMBARGO_DAYS_DEFAULT
        if (execution.hasVariableLocal(EMBARGO_DAYS_PROPERTY)) {
            val variable = execution.getVariableLocal(EMBARGO_DAYS_PROPERTY)
            val longVariable = variable?.toString()?.toLongOrNull()
            if (longVariable != null) embargoDays = longVariable
        }
        val embargoDate = now.plus(embargoDays, ChronoUnit.DAYS)
        execution.setVariableLocal(ISO_EMBARGO_DATE_PROPERTY, embargoDate.toString())

        // The date on which auto escalation will occur.
        var autoEscalationDays = AUTO_ESCALATION_DAYS_DEFAULT
        if (execution.hasVariableLocal(AUTO_ESCALATION_DAYS_PROPERTY)) {
            val variable = execution.getVariableLocal(AUTO_ESCALATION_DAYS_PROPERTY)
            val longVariable = variable?.toString()?.toLongOrNull()
            if (longVariable != null) autoEscalationDays = longVariable
        }
        val escalationDate = now.plus(autoEscalationDays, ChronoUnit.DAYS)
        execution.setVariableLocal(ISO_AUTO_ESCALATION_DATE_PROPERTY, escalationDate.toString())

        // The date that the current task is due.
        var responseDueDays = RESPONSE_DAYS_DEFAULT
        if(execution.hasVariableLocal(RESPONSE_DUE_DAYS_PROPERTY)) {
            val variable = execution.getVariableLocal(RESPONSE_DUE_DAYS_PROPERTY)
            val longVariable = variable?.toString()?.toLongOrNull()
            if(longVariable != null) responseDueDays = longVariable
        }
        val responseDueDate = now.plus(responseDueDays, ChronoUnit.DAYS)
        execution.setVariableLocal(ISO_RESPONSE_DUE_DATE_PROPERTY, responseDueDate.toString())

    }

}
