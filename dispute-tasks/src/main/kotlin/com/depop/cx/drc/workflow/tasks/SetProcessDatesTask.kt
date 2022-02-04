package com.depop.cx.drc.workflow.tasks

import org.camunda.bpm.engine.delegate.DelegateExecution
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private const val ISO_CREATED_DATE_PROPERTY = "created_date"
private const val ISO_RESPONSE_DUE_DATE_PROPERTY = "response_due_date"
private const val ISO_RESPONDED_DATE_PROPERTY = "responded_date"
private const val ISO_EMBARGO_DATE_PROPERTY = "embargo_date"
private const val ISO_AUTO_ESCALATION_DATE_PROPERTY = "auto_escalation_date"
private const val ISO_ESCALATED_DATE_PROPERTY = "escalated_date"
private const val ISO_RESOLVED_DATE_PROPERTY = "resolved_date"

private const val FMT_CREATED_DATE_PROPERTY = "fmt_created_date"
private const val FMT_RESPONSE_DUE_DATE_PROPERTY = "fmt_response_due_date"
private const val FMT_EMBARGO_DATE_PROPERTY = "fmt_embargo_date"
private const val FMT_AUTO_ESCALATION_DATE_PROPERTY = "fmt_auto_escalation_date"
private const val FMT_ESCALATED_DATE_PROPERTY = "fmt_escalated_date"
private const val FMT_RESPONDED_DATE_PROPERTY = "fmt_responded_date"
private const val FMT_RESOLVED_DATE_PROPERTY = "fmt_resolved_date"

private const val RESPONSE_DUE_DAYS_PROPERTY = "response_due_days"
private const val EMBARGO_DAYS_PROPERTY = "embargo_days"
private const val AUTO_ESCALATION_DAYS_PROPERTY = "auto_escalation_days"

private const val EMBARGO_DAYS_DEFAULT = 7L
private const val AUTO_ESCALATION_DAYS_DEFAULT = 30L
private const val RESPONSE_DAYS_DEFAULT = 3L

class SetProcessDatesTask() : AbstractTask() {

    val friendlyFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")

    override fun doExecute(execution: DelegateExecution) {

        val now = OffsetDateTime.ofInstant(
            Instant.now(),
            ZoneId.of("UTC")
        )

        // The date that the dispute was created, i.e. now.
        val createdDate = now
        execution.setVariableLocal(ISO_CREATED_DATE_PROPERTY, createdDate.toString())
        execution.setVariableLocal(FMT_CREATED_DATE_PROPERTY, friendlyFormatter.format(createdDate))

        // The date of the last response.
        val respondedDate = now
        execution.setVariableLocal(ISO_RESPONDED_DATE_PROPERTY, respondedDate.toString())
        execution.setVariableLocal(FMT_RESPONDED_DATE_PROPERTY, friendlyFormatter.format(respondedDate))

        // The date the issue was resolved.
        val resolvedDate = now
        execution.setVariableLocal(ISO_RESOLVED_DATE_PROPERTY, resolvedDate.toString())
        execution.setVariableLocal(FMT_RESOLVED_DATE_PROPERTY, friendlyFormatter.format(resolvedDate))

        // The date the issue was resolved.
        val escalatedDate = now
        execution.setVariableLocal(ISO_ESCALATED_DATE_PROPERTY, escalatedDate.toString())
        execution.setVariableLocal(FMT_ESCALATED_DATE_PROPERTY, friendlyFormatter.format(escalatedDate))

        // The date on which the user can open a dispute.
        var embargoDays = EMBARGO_DAYS_DEFAULT
        if (execution.hasVariableLocal(EMBARGO_DAYS_PROPERTY)) {
            val variable = execution.getVariableLocal(EMBARGO_DAYS_PROPERTY)
            val longVariable = variable?.toString()?.toLongOrNull()
            if (longVariable != null) embargoDays = longVariable
        }
        val embargoDate = now.plus(embargoDays, ChronoUnit.DAYS)
        execution.setVariableLocal(ISO_EMBARGO_DATE_PROPERTY, embargoDate.toString())
        execution.setVariableLocal(FMT_EMBARGO_DATE_PROPERTY, friendlyFormatter.format(embargoDate))

        // The date on which auto escalation will occur.
        var autoEscalationDays = AUTO_ESCALATION_DAYS_DEFAULT
        if (execution.hasVariableLocal(AUTO_ESCALATION_DAYS_PROPERTY)) {
            val variable = execution.getVariableLocal(AUTO_ESCALATION_DAYS_PROPERTY)
            val longVariable = variable?.toString()?.toLongOrNull()
            if (longVariable != null) autoEscalationDays = longVariable
        }
        val escalationDate = now.plus(autoEscalationDays, ChronoUnit.DAYS)
        execution.setVariableLocal(ISO_AUTO_ESCALATION_DATE_PROPERTY, escalationDate.toString())
        execution.setVariableLocal(FMT_AUTO_ESCALATION_DATE_PROPERTY, friendlyFormatter.format(escalationDate))

        // The date that the current task is due.
        var responseDueDays = RESPONSE_DAYS_DEFAULT
        if(execution.hasVariableLocal(RESPONSE_DUE_DAYS_PROPERTY)) {
            val variable = execution.getVariableLocal(RESPONSE_DUE_DAYS_PROPERTY)
            val longVariable = variable?.toString()?.toLongOrNull()
            if(longVariable != null) responseDueDays = longVariable
        }
        val responseDueDate = now.plus(responseDueDays, ChronoUnit.DAYS)
        execution.setVariableLocal(ISO_RESPONSE_DUE_DATE_PROPERTY, responseDueDate.toString())
        execution.setVariableLocal(FMT_RESPONSE_DUE_DATE_PROPERTY, friendlyFormatter.format(responseDueDate))

    }

}
