@file:Suppress("ImplicitSubclassInspection")

package com.depop.cx.drc.workflow.config

import com.depop.cx.drc.workflow.client.*
import com.depop.cx.drc.workflow.listener.DefaultProcessStartListener
import com.depop.cx.drc.workflow.tasks.*
import com.depop.cx.drc.workflow.tasks.comms.SendChatTask
import com.depop.cx.drc.workflow.tasks.comms.SendEmailTask
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
    fun getUserDetailsTask(userClient: UserClient) = GetUserDetailsTask(userClient)

    @Bean
    fun setDisputeParticipantTask(drcClient: DrcClient) = SetDisputeParticipantTask(drcClient)

    @Bean
    fun setDisputeStatusTask(drcClient: DrcClient) = SetDisputeStatusTask(drcClient)

    @Bean
    fun setProcessDatesTask() = SetProcessDatesTask()

    @Bean
    fun updateContextTask() = UpdateContextTask()

    @Bean
    fun bannedUserTask(userClient: UserClient) = GetUserDetailsTask(userClient)

    @Bean
    fun defaultProcessStartListener() = DefaultProcessStartListener()

    @Bean
    fun sendEmailTask(commsClient: CommsClient) = SendEmailTask(commsClient)

    @Bean
    fun sendChatTask(commsClient: CommsClient) = SendChatTask(commsClient)

    @Bean
    fun linkImagesTask(pictureClient: PictureClient) = LinkImagesTask(pictureClient)

}