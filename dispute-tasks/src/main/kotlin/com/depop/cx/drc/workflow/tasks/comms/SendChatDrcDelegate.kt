package com.depop.cx.drc.workflow.tasks.comms

import com.depop.cx.drc.workflow.client.Channel
import com.depop.cx.drc.workflow.client.CommsClient
import com.depop.cx.drc.workflow.metrics.TaskMetrics

class SendChatDrcDelegate(commsClient: CommsClient, private val taskMetrics: TaskMetrics) : AbstractCommsDrcDelegate(commsClient, taskMetrics) {
    override fun getChannel(): Channel {
        return Channel.CHAT
    }
}