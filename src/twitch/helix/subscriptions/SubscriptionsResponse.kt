package de.filtik.gronkhtv.twitch.helix.subscriptions

import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import de.filtik.gronkhtv.twitch.helix.subscriptions.model.Subscription
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

class SubscriptionsResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<Subscription>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<Subscription> = runBlocking {
        httpResponse.receive<HelixArrayDTO<Subscription>>()
    }

    override suspend fun nextPage(): SubscriptionsResponse? =
        nextPageHttpResponse()?.let {
            SubscriptionsResponse(
                it, httpClient
            )
        }
}