package com.depop.cx.drc.workflow

import org.camunda.bpm.engine.delegate.DelegateExecution

data class DelegateFailureMetadata(
    val processDefinitionKey: String?,
    val processDefinitionVersion: String?,
    val activityId: String?,
    val activityName: String?,
    val delegateClassName: String,
    val exceptionClassName: String
) {
    companion object {
        fun from(
            execution: DelegateExecution?,
            exception: Throwable,
            delegateClass: Class<*>
        ): DelegateFailureMetadata {
            val processDefinitionId = execution?.processDefinitionId
            return DelegateFailureMetadata(
                processDefinitionKey = extractProcessDefinitionKey(processDefinitionId),
                processDefinitionVersion = extractProcessDefinitionVersion(processDefinitionId),
                activityName = execution?.currentActivityName?.let { sanitiseActivityName(it) },
                activityId = execution?.currentActivityId,
                delegateClassName = delegateClass.simpleName,
                exceptionClassName = exception::class.java.simpleName
            )
        }

        private val WHITESPACE_REGEX = "\\s+".toRegex()
        private val NON_ALPHANUMERIC_REGEX = "[^a-z0-9_]".toRegex()

        fun sanitiseActivityName(activityName: String): String? {
            return activityName
                .lowercase()
                .trim()
                .replace(WHITESPACE_REGEX   , "_") // spaces to underscore
                .replace(NON_ALPHANUMERIC_REGEX, "") // strip symbols other than _
                .takeIf { it.isNotBlank() } // if we're left with nothing, return null
        }

        /*
        An example of a process definition ID is `Process_0oxrtl6:1:9eb9c9f0-3f98-11f0-88e8-62dcf9e7fde`
        Where Process_0oxrtl6 is the process definition key, and :1: is the process definition version.
        This may be specific to Camunda 7.
        But it's useful information, and the alternative way of getting this involves hitting the Camunda DB.
         */
        fun extractProcessDefinitionKey(processDefinitionId: String?): String? {
           return processDefinitionId?.split(":")?.getOrNull(0)
        }

        fun extractProcessDefinitionVersion(processDefinitionId: String?): String? {
            val versionPart = processDefinitionId?.split(":")?.getOrNull(1)
            return versionPart?.takeIf { it.toIntOrNull() != null }
        }
    }
}
