package com.depop.cx.drc.workflow

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.variable.type.ValueType
import org.camunda.bpm.engine.variable.value.LongValue
import org.camunda.bpm.engine.variable.value.StringValue
import org.camunda.bpm.engine.variable.value.TypedValue
import java.util.*

fun DelegateExecution.getLongVariableOrNull(variable: String): Long? {
    if (this.hasVariableLocal(variable)) {
        val value: TypedValue = this.getVariableTyped(variable)
        if(ValueType.LONG.equals(value.type)) {
            return value.value as Long
        } else if (ValueType.LONG.canConvertFromTypedValue(value)) {
            val result = ValueType.LONG.convertFromTypedValue(value) as LongValue
            return result.value
        } else if (ValueType.STRING == value.type) {
            val result = value as StringValue
            return result.value.toLongOrNull()
        }
    }
    return null
}

fun DelegateExecution.getStringVariableOrNull(variable: String): String? {
    if (this.hasVariableLocal(variable)) {
        val value: TypedValue = this.getVariableTyped(variable)
        if (ValueType.STRING == value.type) {
            val result = value as StringValue
            return result.value
        }
    }
    return null
}

fun DelegateExecution.getUUIDVariableOrNull(variable: String): UUID? {
    val value = this.getStringVariableOrNull(variable)
    if(value != null) {
        try {
            return UUID.fromString(value)
        } catch(e : IllegalArgumentException) {
            // Do nothing
        }
    }
    return null
}

fun DelegateExecution.getStringProcessVariableOrNull(variable: String): String? {
    if (this.hasVariable(variable)) {
        val value: TypedValue = this.getVariableTyped(variable)
        if (ValueType.STRING == value.type) {
            val result = value as StringValue
            return result.value
        }
    }
    return null
}

fun DelegateExecution.getUUIDProcessVariableOrNull(variable: String): UUID? {
    val value = this.getStringProcessVariableOrNull(variable)
    if(value != null) {
        try {
            return UUID.fromString(value)
        } catch(e : IllegalArgumentException) {
            // Do nothing
        }
    }
    return null
}