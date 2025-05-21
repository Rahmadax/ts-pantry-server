package com.depop.cx.drc.workflow.tasks.timer

import com.depop.cx.drc.workflow.AbstractDrcDelegate
import com.depop.cx.drc.workflow.getStringVariableOrNull
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.impl.persistence.entity.TimerEntity

private const val TIMER_ID_PARAM = "timer_id"

abstract class AbstractTimerDelegate : AbstractDrcDelegate() {

    private val logger = KotlinLogging.logger {}

    override fun doExecute(execution: DelegateExecution) {
        val timerId = execution.getStringVariableOrNull(TIMER_ID_PARAM)
        if (timerId == null) {
            throw IllegalArgumentException("variable $TIMER_ID_PARAM is not set")
        }

        val managementService = execution.processEngineServices.managementService

        val rawTimers = managementService.createJobQuery().processInstanceId(execution.processInstanceId).timers().list().filterIsInstance<TimerEntity>()
        rawTimers.forEach {
            val configStr = it.jobHandlerConfiguration?.toCanonicalString()
            logger.info { "banana Raw timer: id=${it.id}, activityId=$configStr" }
        }

        val filteredTimers = rawTimers.filter {
            val configStr = it.jobHandlerConfiguration?.toCanonicalString()
            logger.info { "banana Comparing '$timerId' to '$configStr'" }
            timerId == configStr
        }

        logger.info { "banana ${filteredTimers.size} timers found" }

        when (filteredTimers.size) {
            1 -> doTimerExecute(execution, filteredTimers.single())
            else -> throw IllegalStateException("${filteredTimers.size} timers found with id $timerId")
        }
    }

    abstract fun doTimerExecute(execution: DelegateExecution, timer: TimerEntity)

}