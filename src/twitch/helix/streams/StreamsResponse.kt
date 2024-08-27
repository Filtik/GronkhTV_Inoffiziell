package de.filtik.gronkhtv.twitch.helix.streams

import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import de.filtik.gronkhtv.twitch.helix.streams.model.Stream
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

/**
 * A collection response that holds one or multiple stream resources as payload.
 * The resource collection can be split into multiple pages.
 * @constructor Creates a new streams response object.
 */
class StreamsResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<Stream>(httpResponse = httpResponse, httpClient = httpClient) {
    override val helixArrayDTO: HelixArrayDTO<Stream> = runBlocking {
        httpResponse.receive<HelixArrayDTO<Stream>>()
    }

    override suspend fun nextPage(): StreamsResponse? =
        nextPageHttpResponse("after")?.let {
            StreamsResponse(
                it, httpClient
            )
        }
}