@file:Suppress("ImplicitSubclassInspection")

package com.depop.cx.drc.workflow.config

import com.depop.cx.drc.workflow.client.*
import com.depop.cx.drc.workflow.listener.*
import com.depop.cx.drc.workflow.tasks.*
import com.depop.cx.drc.workflow.tasks.comms.SendChatDrcDelegate
import com.depop.cx.drc.workflow.tasks.comms.SendEmailDrcDelegate
import com.depop.cx.drc.workflow.tasks.comms.SendPushDrcDelegate
import com.depop.cx.drc.workflow.tasks.timer.ActivateTimerDelegate
import com.depop.cx.drc.workflow.tasks.timer.RecalculateTimerDelegate
import com.depop.cx.drc.workflow.tasks.timer.SuspendTimerDelegate
import com.depop.cx.drc.workflow.tasks.timer.UpdateResponseDueDateDelegate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WorkflowTaskConfiguration {

    @Bean
    fun getReceiptDetailsTask(
        checkoutClient: CheckoutClient,
        paymentsClient: PaymentsClient,
        shippingClient: ShippingClient
    ) = GetReceiptDetailsDrcDelegate(checkoutClient, paymentsClient, shippingClient)

    @Bean
    fun activateTimerTask() = ActivateTimerDelegate()

    @Bean
    fun suspendTimerTask() = SuspendTimerDelegate()

    @Bean
    fun recalculateTimerTask() = RecalculateTimerDelegate()

    @Bean
    fun updateResponseDueDateTask() = UpdateResponseDueDateDelegate()

    @Bean
    fun getUserDetailsTask(userClient: UserClient) = GetUserDetailsDrcDelegate(userClient)

    @Bean
    fun getUserBlockedTask(blockingClient: BlockingClient) = GetUserBlockedDelegate(blockingClient)

    @Bean
    fun getProductDetailsTask(productClient: ProductClient, checkoutClient: CheckoutClient) = GetProductDetailsDrcDelegate(productClient, checkoutClient)

    @Bean
    fun setDisputeParticipantTask(drcClient: DrcClient) = SetDisputeParticipantDrcDelegate(drcClient)

    @Bean
    fun setDisputeStatusTask(drcClient: DrcClient) = SetDisputeStatusDrcDelegate(drcClient)

    @Bean
    fun setProcessDatesTask() = SetProcessDatesDrcDelegate()

    @Bean
    fun updateContextTask() = UpdateContextDrcDelegate()

    @Bean
    fun bannedUserTask(userClient: UserClient) = GetUserDetailsDrcDelegate(userClient)

    @Bean
    fun defaultProcessStartListener() = DefaultProcessStartListener()

    @Bean
    fun sendEmailTask(commsClient: CommsClient) = SendEmailDrcDelegate(commsClient)

    @Bean
    fun sendChatTask(commsClient: CommsClient) = SendChatDrcDelegate(commsClient)

    @Bean
    fun sendPushTask(commsClient: CommsClient) = SendPushDrcDelegate(commsClient)

    @Bean
    fun linkImagesTask(pictureClient: PictureClient) = LinkImagesDrcDelegate(pictureClient)

    @Bean
    fun setActiveUserIdTask(drcClient: DrcClient) = SetDisputeActiveUserIdDrcDelegate(drcClient)

    @Bean
    fun clearActiveUserIdTask(drcClient: DrcClient) = ClearDisputeActiveUserIdDrcDelegate(drcClient)

    @Bean
    fun setResponseDueDateTask(drcClient: DrcClient) = SetResponseDueDateDrcDelegate(drcClient)

    @Bean
    fun clearResponseDueDateTask(drcClient: DrcClient) = ClearResponseDueDateDrcDelegate(drcClient)

}