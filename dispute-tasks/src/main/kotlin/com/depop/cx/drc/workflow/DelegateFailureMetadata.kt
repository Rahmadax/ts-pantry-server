package com.depop.cx.drc.workflow

import org.camunda.bpm.engine.delegate.DelegateExecution

data class DelegateFailureMetadata(
    val processDefinitionId: String?,
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
            return DelegateFailureMetadata(
                processDefinitionId = execution?.processDefinitionId,
                processDefinitionVersion = execution?.let { extractProcessDefinitionVersion(it.processDefinitionId) },
                activityName = execution?.currentActivityName?.let { sanitiseActivityName(it) },
                activityId = execution?.currentActivityId,
                delegateClassName = delegateClass.simpleName,
                exceptionClassName = exception::class.java.simpleName
            )
        }

        fun sanitiseActivityName(activityName: String): String? {
            return activityName
                .lowercase()
                .trim()
                .replace("\\s+".toRegex(), "_") // spaces to underscore
                .replace("[^a-z0-9_]".toRegex(), "") // strip symbols other than _
                .takeIf { it.isNotBlank() } // if we're left with nothing, return null
        }

        fun extractProcessDefinitionVersion(processDefinitionId: String?): String? {
            /*
            An example of a process definition ID is `Process_0oxrtl6:1:9eb9c9f0-3f98-11f0-88e8-62dcf9e7fde`
            Where :1: is the process definition version.
            This may be specific to Camunda 7.
            But it's useful information, and the alternative way of getting this involves hitting the Camunda DB.
             */
            val versionPart = processDefinitionId?.split(":")?.getOrNull(1)
            return versionPart?.takeIf { it.toIntOrNull() != null }
        }
    }
}
