package de.filtik.gronkhtv.twitch.helix.channels

import de.filtik.gronkhtv.twitch.helix.channels.model.hypetrain.HypeTrain
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


/**
 * A collection response has one or multiple pages of hype train events as payload.
 * @constructor Creates a new hype train response object.
 */

class HypeTrainEventsResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<HypeTrain>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<HypeTrain> = runBlocking {
        httpResponse.receive<HelixArrayDTO<HypeTrain>>()
    }

    override suspend fun nextPage(): HypeTrainEventsResponse? =
        nextPageHttpResponse()?.let {
            HypeTrainEventsResponse(it, httpClient)
        }
}