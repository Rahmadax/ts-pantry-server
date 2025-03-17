package com.depop.cx.drc.workflow.tasks.comms

import com.depop.cx.drc.workflow.client.Channel
import com.depop.cx.drc.workflow.client.CommsClient

class SendPushTask(commsClient: CommsClient) : AbstractCommsTask(commsClient) {
    override fun getChannel(): Channel {
        return Channel.PUSH
    }
}