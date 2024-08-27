package de.filtik.gronkhtv.twitch.helix.moderation

import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import de.filtik.gronkhtv.twitch.helix.moderation.model.ModeratorEvent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


class ModeratorEventsScrollableResponse(
    httpResponse: HttpResponse,
    httpClient: HttpClient
) :
    ScrollableResponse<ModeratorEvent>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<ModeratorEvent> = runBlocking {
        httpResponse.receive<HelixArrayDTO<ModeratorEvent>>()
    }

    override suspend fun nextPage(): ModeratorEventsScrollableResponse? =
        nextPageHttpResponse(cursorKey = "after")?.let {
            ModeratorEventsScrollableResponse(
                it, httpClient
            )
        }

}