package com.depop.cx.drc.workflow.client

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

abstract class Client(protected val webClient: WebClient) {

    protected inline fun <reified T> getRequest(
        uri: String,
        variables: Map<String, *>
    ): Mono<T> = webClient
        .get()
        .uri(uri, variables)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(T::class.java)

    protected suspend inline fun <reified T: Any> getCoroutineRequest(
        uri: String,
        variables: Map<String, *>
    ): T = webClient
        .get()
        .uri(uri, variables)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .awaitBody<T>()

    protected inline fun <reified V : Any, reified T> postRequest(
        uri: String,
        request: V
    ): Mono<T> = webClient.post()
        .uri(uri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(request), V::class.java)
        .retrieve()
        .bodyToMono(T::class.java)

    protected inline fun <reified V : Any, reified T> postRequest(
        uri: String,
        request: V,
        variables: Map<String, *>
    ): Mono<T> = webClient.post()
        .uri(uri, variables)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(request), V::class.java)
        .retrieve()
        .bodyToMono(T::class.java)

    protected suspend inline fun <reified V : Any, reified T: Any> putCoroutineRequest(
        uri: String,
        request: V,
        variables: Map<String, *>
    ): T = webClient.put()
        .uri(uri, variables)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
        .awaitBody<T>()

}