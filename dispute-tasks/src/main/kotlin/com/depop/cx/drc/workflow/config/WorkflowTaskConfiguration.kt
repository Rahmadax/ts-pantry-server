@file:Suppress("ImplicitSubclassInspection")

package com.depop.cx.drc.workflow.config

import com.depop.cx.drc.workflow.client.*
import com.depop.cx.drc.workflow.listener.*
import com.depop.cx.drc.workflow.metrics.TaskMetrics
import com.depop.cx.drc.workflow.tasks.*
import com.depop.cx.drc.workflow.tasks.comms.SendChatDrcDelegate
import com.depop.cx.drc.workflow.tasks.comms.SendEmailDrcDelegate
import com.depop.cx.drc.workflow.tasks.comms.SendPushDrcDelegate
import com.depop.cx.drc.workflow.tasks.timer.ActivateTimerDelegate
import com.depop.cx.drc.workflow.tasks.timer.RecalculateTimerDelegate
import com.depop.cx.drc.workflow.tasks.timer.SuspendTimerDelegate
import com.depop.cx.drc.workflow.tasks.timer.UpdateResponseDueDateDelegate
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.logging.LoggingMeterRegistry
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.Scope

@Configuration
class WorkflowTaskConfiguration {

    @Bean
    fun taskMetrics(meterRegistry: MeterRegistry) = TaskMetrics(meterRegistry)

    @Bean
    @Profile("local")
    fun loggingMeterRegistry() = LoggingMeterRegistry()

    @Bean
    fun getReceiptDetailsTask(
        checkoutClient: CheckoutClient,
        paymentsClient: PaymentsClient,
        shippingClient: ShippingClient,
        taskMetrics: TaskMetrics
    ) = GetReceiptDetailsDrcDelegate(checkoutClient, paymentsClient, shippingClient, taskMetrics)

    @Bean
    fun activateTimerTask(taskMetrics: TaskMetrics) = ActivateTimerDelegate(taskMetrics)

    @Bean
    fun suspendTimerTask(taskMetrics: TaskMetrics) = SuspendTimerDelegate(taskMetrics)

    @Bean
    fun recalculateTimerTask(taskMetrics: TaskMetrics) = RecalculateTimerDelegate(taskMetrics)

    @Bean
    fun updateResponseDueDateTask(taskMetrics: TaskMetrics) = UpdateResponseDueDateDelegate(taskMetrics)

    @Bean
    fun getUserDetailsTask(userClient: UserClient, taskMetrics: TaskMetrics) = GetUserDetailsDrcDelegate(userClient, taskMetrics)

    @Bean
    fun getUserBlockedTask(blockingClient: BlockingClient, taskMetrics: TaskMetrics) = GetUserBlockedDelegate(blockingClient, taskMetrics)

    @Bean
    fun getProductDetailsTask(productClient: ProductClient, checkoutClient: CheckoutClient, taskMetrics: TaskMetrics) = GetProductDetailsDrcDelegate(productClient, checkoutClient, taskMetrics)

    @Bean
    fun getRefundDetailsTask(checkoutClient: CheckoutClient, taskMetrics: TaskMetrics) = GetRefundDetailsDrcDelegate(checkoutClient, taskMetrics)

    @Bean
    fun setDisputeParticipantTask(drcClient: DrcClient, taskMetrics: TaskMetrics) = SetDisputeParticipantDrcDelegate(drcClient, taskMetrics)

    @Bean
    fun getSellerAddressDetailsTask(addressClient: AddressClient, taskMetrics: TaskMetrics) = GetSellerAddressDrcDelegate(addressClient, taskMetrics)

    @Bean
    fun setDisputeStatusTask(drcClient: DrcClient, taskMetrics: TaskMetrics) = SetDisputeStatusDrcDelegate(drcClient, taskMetrics)

    @Bean
    fun setProcessDatesTask(taskMetrics: TaskMetrics) = SetProcessDatesDrcDelegate(taskMetrics)

    @Bean
    fun updateContextTask(taskMetrics: TaskMetrics) = UpdateContextDrcDelegate(taskMetrics)

    @Bean
    fun bannedUserTask(userClient: UserClient, taskMetrics: TaskMetrics) = GetUserDetailsDrcDelegate(userClient, taskMetrics)

    @Bean
    fun defaultProcessStartListener() = DefaultProcessStartListener()

    @Bean
    fun sendEmailTask(commsClient: CommsClient, taskMetrics: TaskMetrics) = SendEmailDrcDelegate(commsClient, taskMetrics)

    @Bean
    fun sendChatTask(commsClient: CommsClient, taskMetrics: TaskMetrics) = SendChatDrcDelegate(commsClient, taskMetrics)

    @Bean
    fun sendPushTask(commsClient: CommsClient, taskMetrics: TaskMetrics) = SendPushDrcDelegate(commsClient, taskMetrics)

    @Bean
    fun linkImagesTask(pictureClient: PictureClient, taskMetrics: TaskMetrics) = LinkImagesDrcDelegate(pictureClient, taskMetrics)

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun setActiveUserIdTask(drcClient: DrcClient, taskMetrics: TaskMetrics) = SetDisputeActiveUserIdDrcDelegate(drcClient, taskMetrics)

    @Bean
    fun clearActiveUserIdTask(drcClient: DrcClient, taskMetrics: TaskMetrics) = ClearDisputeActiveUserIdDrcDelegate(drcClient, taskMetrics)

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun setResponseDueDateTask(drcClient: DrcClient, taskMetrics: TaskMetrics) = SetResponseDueDateDrcDelegate(drcClient, taskMetrics)

    @Bean
    fun clearResponseDueDateTask(drcClient: DrcClient, taskMetrics: TaskMetrics) = ClearResponseDueDateDrcDelegate(drcClient, taskMetrics)

}