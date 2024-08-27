package de.filtik.gronkhtv.twitch.helix.analytics

import de.filtik.gronkhtv.twitch.helix.analytics.model.GameReport
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

/**
 * A collection response that has one or multiple pages of game reports as payload.
 * @constructor Creates a new games analytics response object.
 */

class GameAnalyticsScrollableResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<GameReport>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<GameReport> = runBlocking {
        httpResponse.receive<HelixArrayDTO<GameReport>>()
    }

    override suspend fun nextPage(): GameAnalyticsScrollableResponse? =
        nextPageHttpResponse(cursorKey = "after")?.let {
            GameAnalyticsScrollableResponse(
                it, httpClient
            )
        }
}