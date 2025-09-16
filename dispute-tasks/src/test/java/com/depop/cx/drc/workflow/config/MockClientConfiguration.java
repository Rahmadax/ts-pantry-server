package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.client.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static com.depop.cx.drc.workflow.InadTestFixtures.*;
import static org.mockito.Mockito.*;

/*
NOTE: these mocks and default stubs are used in workflow tests

they may appear to be unused, BUT, they are published through the CI pipeline and used in the
workflow tests defined in [dispute-definitions](https://github.com/depop/dispute-definitions)

see
https://github.com/depop/dispute-definitions/blob/master/TESTING.md
for more information
 */
@TestConfiguration(proxyBeanMethods = false)
@MockBean(
        value = {
                CheckoutClient.class,
                PaymentsClient.class,
                ShippingClient.class,
                UserClient.class,
                AddressClient.class,
                ProductClient.class,
                DrcClient.class,
                CommsClient.class,
                PictureClient.class,
        }
)
public class MockClientConfiguration {

    @FunctionalInterface
    public interface MockDefaults {
        void apply();
    }

    @Bean
    protected MockDefaults mockDefaults(final ApplicationContext ctx) {
        return () -> {
            final var drcClient = ctx.getBean(DrcClient.class);
            final var userClient = ctx.getBean(UserClient.class);
            final var addressClient = ctx.getBean(AddressClient.class);
            final var shippingClient = ctx.getBean(ShippingClient.class);
            final var productClient = ctx.getBean(ProductClient.class);
            final var commsClient = ctx.getBean(CommsClient.class);

            when(drcClient.updateDispute(any(UUID.class), any(UpdateDisputeRequest.class))).thenReturn(Mono.empty());
            when(drcClient.setDisputeParticipant(any(), eq(BUYER_ID), anyString())).thenReturn(Mono.empty());
            when(drcClient.setDisputeParticipant(any(), eq(SELLER_ID), anyString())).thenReturn(Mono.empty());
            when(drcClient.setDisputeParticipant(any(), eq(BLOCKED_BUYER_ID), anyString())).thenReturn(Mono.empty());
            when(drcClient.setDisputeParticipant(any(), eq(BLOCKED_SELLER_ID), anyString())).thenReturn(Mono.empty());
            when(drcClient.setDisputeStatus(any(UUID.class), anyString())).thenReturn(Mono.empty());
            when(userClient.getUser(BUYER_ID)).thenReturn(Mono.just(BUYER));
            when(userClient.getUser(SELLER_ID)).thenReturn(Mono.just(SELLER));
            when(commsClient.send(any(), anyLong(), any(), any(), anyString())).thenReturn(Mono.empty());
            when(userClient.getUser(BLOCKED_BUYER_ID)).thenReturn(Mono.just(BUYER));
            when(userClient.getUser(BLOCKED_SELLER_ID)).thenReturn(Mono.just(SELLER));
            when(addressClient.getAddress(ADDRESS_ID)).thenReturn(Mono.just(ADDRESS));
            when(addressClient.getUserAddresses(SELLER_ID)).thenReturn(Mono.just(List.of(ADDRESS)));
            final var parcels = new Parcels();
            parcels.put(PARCEL_ID, PARCEL_DETAILS);
            when(shippingClient.getParcelDetails(List.of(PARCEL_ID))).thenReturn(Mono.just(parcels));
            when(productClient.getProduct(PRODUCT.getId())).thenReturn(Mono.just(PRODUCT));
        };
    }

}
