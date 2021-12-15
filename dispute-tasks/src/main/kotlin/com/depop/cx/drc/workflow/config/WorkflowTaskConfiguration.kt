@file:Suppress("ImplicitSubclassInspection")

package com.depop.cx.drc.workflow.config

import com.depop.cx.drc.workflow.client.CheckoutClient
import com.depop.cx.drc.workflow.client.DrcClient
import com.depop.cx.drc.workflow.client.PaymentsClient
import com.depop.cx.drc.workflow.client.ShippingClient
import com.depop.cx.drc.workflow.tasks.GetReceiptDetailsTask
import com.depop.cx.drc.workflow.tasks.SetDisputeParticipantTask
import com.depop.cx.drc.workflow.tasks.SetDisputeStatusTask
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

}