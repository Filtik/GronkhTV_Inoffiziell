package de.filtik.gronkhtv.twitch.helix.analytics

import de.filtik.gronkhtv.twitch.helix.analytics.model.ExtensionReport
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.runBlocking

/**
 * A collection response has one or multiple pages of extension reports as payload.
 * @constructor Creates a new extension analytics response object.
 */

class ExtensionAnalyticsScrollableResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<ExtensionReport>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<ExtensionReport> = runBlocking {
        httpResponse.receive<HelixArrayDTO<ExtensionReport>>()
    }

    override suspend fun nextPage(): ExtensionAnalyticsScrollableResponse? =
        nextPageHttpResponse(cursorKey = "after")?.let {
            ExtensionAnalyticsScrollableResponse(
                it, httpClient
            )
        }
}