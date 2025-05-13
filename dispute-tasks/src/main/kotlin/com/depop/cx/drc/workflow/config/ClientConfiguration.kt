@file:Suppress("ImplicitSubclassInspection")

package com.depop.cx.drc.workflow.config

import com.depop.cx.drc.workflow.client.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.client.*
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI

private const val DEPOP_CLIENT_REGISTRATION_ID = "depop"
private const val DEPOP_GRANT_TYPE = "http://depop.com/oauth/grant-type/client_credentials"
private const val DEPOP_JWT_HEADER = "x-authorization-jwt"

@ConfigurationProperties("depop.drc.client")
data class ClientProperties(
    val checkoutHost: URI,
    val drcHost: URI,
    val paymentsHost: URI,
    val shippingHost: URI,
    val shippingApiSecret: String,
    val userHost: URI,
    val commsHost: URI,
    val pictureHost: URI,
    val productHost: URI,
)

@Configuration
@EnableConfigurationProperties(ClientProperties::class)
class ClientConfiguration {

    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        webClientBuilder: WebClient.Builder
    ): ReactiveOAuth2AuthorizedClientManager {

        //NOTE: Some of this is only required because the project uses spring MVC for the camunda api.
        // Spring boot configures the non-reactive oauth2 objects in preference to the reactive ones.
        // this configuration manually sets up the necessary reactive components that the non-blocking
        // web client needs.  If you don't use MVC most of this is probably not required.
        val client = clientRegistrationRepository.findByRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        // This custom webclient allows us to filter and manipulate the OAuth2 token requests.  We
        // need to do this to work around the idiosyncrasies of the depop auth servers.  See
        // setDepopTokenRequestBody()
        val oAuthTokenClient = webClientBuilder
            .filter(setDepopTokenRequestBody(client.clientId, client.clientSecret))
            .build()

        val clientCredentialsTokenResponseClient = WebClientReactiveClientCredentialsTokenResponseClient()
        clientCredentialsTokenResponseClient.setWebClient(oAuthTokenClient)

        val clientCredentialsProvider = ClientCredentialsReactiveOAuth2AuthorizedClientProvider()
        clientCredentialsProvider.setAccessTokenResponseClient(clientCredentialsTokenResponseClient)

        val authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
            .provider(clientCredentialsProvider)
            .refreshToken()
            .build()

        val clientRegistrationRepo = InMemoryReactiveClientRegistrationRepository(client)
        val authorizedClientService: ReactiveOAuth2AuthorizedClientService =
            InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepo)
        val authorizedClientManager = AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepo,
            authorizedClientService
        )
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

    @Bean
    fun drcClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager
    ): DrcClient {

        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.drcHost.toString())
            .filter(oauth) // filter to add depop jwt to the Authorization header.
            .filter(setDepopCustomAuthHeader(true)) // filter to copy the auth header to x-authorisation-jwt
            .build()

        return DrcClient(webClient)
    }

    @Bean
    fun checkoutClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager
    ): CheckoutClient {

        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.checkoutHost.toString())
            .filter(oauth) // filter to add depop jwt to the Authorization header.
            .filter(setDepopCustomAuthHeader()) // filter to copy the auth header to x-authorisation-jwt
            .build()

        return CheckoutClient(webClient)
    }

    @Bean
    fun paymentsClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager
    ): PaymentsClient {

        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.paymentsHost.toString())
            .filter(oauth) // filter to add depop jwt to the Authorization header.
            .filter(setDepopCustomAuthHeader()) // filter to copy the auth header to x-authorisation-jwt
            .build()

        return PaymentsClient(webClient)
    }

    @Bean
    fun shippingClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager
    ): ShippingClient {

        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.shippingHost.toString())
            .filter(setDepopLLTokenAuthHeader(clientProperties.shippingApiSecret)) // Shipping api uses shared tokens
            .build()

        return ShippingClient(webClient)
    }

    @Bean
    fun userClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager
    ): UserClient {

        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.userHost.toString())
            .filter(oauth) // filter to add depop jwt to the Authorization header.
            .build()

        return UserClient(webClient)
    }

    @Bean
    fun commsClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager,
    ): CommsClient {

        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.commsHost.toString())
            .filter(oauth) // filter to add depop jwt to the Authorization header.
            .filter(setDepopCustomAuthHeader(true)) // filter to copy the auth header to x-authorisation-jwt
            .build()

        return CommsClient(webClient)
    }

    @Bean
    fun pictureClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager,
    ): PictureClient {
        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oauth.setDefaultClientRegistrationId(DEPOP_CLIENT_REGISTRATION_ID)

        val webClient = webClientBuilder
            .baseUrl(clientProperties.pictureHost.toString())
            .filter(oauth) // filter to add depop jwt to the Authorization header
            .build()

        return PictureClient(webClient)
    }

    @Bean
    fun productClient(
        clientProperties: ClientProperties,
        webClientBuilder: WebClient.Builder,
    ): ProductClient {
        val webClient = webClientBuilder
            .baseUrl(clientProperties.productHost.toString())
            .build()

        return ProductClient(webClient)
    }

    // Sets the Authorization header using the provided LLJWT
    fun setDepopLLTokenAuthHeader(token: String): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { request ->
            val updatedRequest = ClientRequest.from(request).headers { headers ->
                headers.setBearerAuth(token)
            }.build()
            Mono.just(updatedRequest)
        }
    }

    // Copies the Authorization header to a new, x-authorisation-jwt header, optionally removing the standard
    // header.  Different services have different header requirements and this filter gives us flexibility.
    fun setDepopCustomAuthHeader(removeDefaultAuthHeader: Boolean = false): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { request ->
            val updatedRequest = ClientRequest.from(request).headers { headers ->
                val authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION)?.removePrefix("Bearer ")
                headers.add(DEPOP_JWT_HEADER, authHeader)
                if (removeDefaultAuthHeader) {
                    headers.remove(HttpHeaders.AUTHORIZATION)
                }
            }.build()
            Mono.just(updatedRequest)
        }
    }

    // Depop uses a custom grant type for client_credentials flow.  Spring does not support this
    // and spring security is locked down pretty tight, which makes customisation tricky.
    // This filter is a bit hacky but it works.  The filter intercepts the request for the token
    // and discards the body generated by spring (which has grant_type:client_credentials).  It
    // creates a new body that uses the depop custom grant type.  It's a minimally invasive hack and
    // works around the problem.
    fun setDepopTokenRequestBody(clientId: String, clientSecret: String): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { request ->
            val body = BodyInserters.fromFormData(OAuth2ParameterNames.GRANT_TYPE, DEPOP_GRANT_TYPE)
                .with(OAuth2ParameterNames.CLIENT_ID, clientId)
                .with(OAuth2ParameterNames.CLIENT_SECRET, clientSecret)
            val updatedRequest = ClientRequest.from(request).body(body).build()
            Mono.just(updatedRequest)
        }
    }

}