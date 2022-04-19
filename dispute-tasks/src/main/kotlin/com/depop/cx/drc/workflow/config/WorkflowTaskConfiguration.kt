@file:Suppress("ImplicitSubclassInspection")

package com.depop.cx.drc.workflow.config

import com.depop.cx.drc.workflow.client.*
import com.depop.cx.drc.workflow.listener.DefaultProcessStartListener
import com.depop.cx.drc.workflow.tasks.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkflowTaskConfiguration {

    @Bean
    fun getReceiptDetailsTask(
        checkoutClient: CheckoutClient,
        paymentsClient: PaymentsClient,
        shippingClient: ShippingClient
    ) = GetReceiptDetailsTask(checkoutClient, paymentsClient, shippingClient)

    @Bean
    fun setDisputeParticipantTask(drcClient: DrcClient) = SetDisputeParticipantTask(drcClient)

    @Bean
    fun setDisputeStatusTask(drcClient: DrcClient) = SetDisputeStatusTask(drcClient)

    @Bean
    fun setProcessDatesTask() = SetProcessDatesTask()

    @Bean
    fun updateContextTask() = UpdateContextTask()

    @Bean
    fun bannedUserTask(userClient: UserClient) = BannedUserTask(userClient)

    @Bean
    fun defaultProcessStartListener() = DefaultProcessStartListener()

}