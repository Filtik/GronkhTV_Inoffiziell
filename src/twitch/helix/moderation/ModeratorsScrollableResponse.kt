package de.filtik.gronkhtv.twitch.helix.moderation

import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import de.filtik.gronkhtv.twitch.helix.moderation.model.Moderator
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


class ModeratorsScrollableResponse(
    httpResponse: HttpResponse,
    httpClient: HttpClient
) :
    ScrollableResponse<Moderator>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<Moderator> = runBlocking {
        httpResponse.receive<HelixArrayDTO<Moderator>>()
    }

    override suspend fun nextPage(): ModeratorsScrollableResponse? =
        nextPageHttpResponse(cursorKey = "after")?.let {
            ModeratorsScrollableResponse(
                it, httpClient
            )
        }

}