package com.depop.cx.drc.workflow;


import com.depop.cx.drc.workflow.client.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class InadTestFixtures {

    public static final String RECEIPT_ID = "123";
    public static final long SELLER_ID = 666L;
    public static final long BUYER_ID = 333L;
    public static final long BLOCKED_SELLER_ID = 4444L;
    public static final long BLOCKED_BUYER_ID = 555L;

    public static User BUYER = new User(BUYER_ID, true, "buyer");
    public static User SELLER = new User(SELLER_ID, true, "seller");
    public static User SELLER_BANNED = new User(SELLER_ID, false, "seller");

    public static String PRODUCT_STATUS_PURCHASED = "P";

    public static Product PRODUCT = new Product(
            777L,
            PRODUCT_STATUS_PURCHASED
    );

    public static final FullReceiptLineItemResponse RECEIPT_LINE_ITEM =
            new FullReceiptLineItemResponse(888, PRODUCT.getId());

    public static final String PAYMENT_PROVIDER_STRIPE = "STRIPE";
    public static final String PAYMENT_PROVIDER_PAYPAL = "PAYPAL";

    public static final Receipt RECEIPT = new Receipt(
            Long.parseLong(RECEIPT_ID),
            BUYER_ID,
            SELLER_ID,
            ZonedDateTime.now().minusDays(6), // to pass validation
            PAYMENT_PROVIDER_STRIPE,
            123,
            new BigDecimal("1.00"),
            new RefundDetails(new BigDecimal("0.5"), new BigDecimal("0.5"), "GBP"),
            List.of(RECEIPT_LINE_ITEM)
    );


    public static final Payment PAYMENT_REFUNDABLE = new Payment(true);

    public static String SHIPPING_STATUS_SHIPPED = "shipped";

    public static String PARCEL_ID = UUID.randomUUID().toString();
    public static ParcelProviderDetails MANUAL_PARCEL_PROVIDER_DETAILS = new ParcelProviderDetails(
            "IN_TRANSIT",
            "92001902416755000000000016",
            null,
            Instant.now().minus(5, ChronoUnit.DAYS).atZone(ZoneOffset.UTC),
            Instant.now().minus(6, ChronoUnit.DAYS).atZone(ZoneOffset.UTC)
    );
    public static ParcelDetails PARCEL_DETAILS = new ParcelDetails(
            PARCEL_ID,
            MANUAL_PARCEL_PROVIDER_DETAILS
    );

    public static final Long ADDRESS_ID = 123456L;

    public static final Address ADDRESS = new Address(
            ADDRESS_ID,
            "Test Street",
            "Test Town",
            "Test City",
            "Test County",
            "Test Postcode",
            "M15",
            "GB" // ISO 3166-1 alpha-2 country code
    );

}
