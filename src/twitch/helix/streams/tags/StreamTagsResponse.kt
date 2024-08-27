package de.filtik.gronkhtv.twitch.helix.streams.tags

import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import de.filtik.gronkhtv.twitch.helix.streams.tags.model.StreamTag
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.runBlocking

/**
 * A collection response that holds one or multiple stream tag resources as payload.
 * The resource collection can be split into multiple pages.
 * @constructor Creates a new stream tags response object.
 */
class StreamTagsResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<StreamTag>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<StreamTag> = runBlocking {
        httpResponse.receive<HelixArrayDTO<StreamTag>>()
    }

    override suspend fun nextPage(): StreamTagsResponse? =
        nextPageHttpResponse("after")?.let {
            StreamTagsResponse(
                it, httpClient
            )
        }
}