package de.filtik.gronkhtv.twitch.helix.channels

import de.filtik.gronkhtv.twitch.helix.channels.model.Channel
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


/**
 * A collection response has one or multiple pages of channels as payload.
 * @constructor Creates a new channels response object.
 */
class ChannelsResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<Channel>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<Channel> = runBlocking {
        httpResponse.receive()
    }

    override suspend fun nextPage(): ChannelsResponse? =
        nextPageHttpResponse("after")?.let {
            ChannelsResponse(
                it, httpClient
            )
        }
}