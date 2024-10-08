package de.filtik.gronkhtv.twitch.helix.bits

import de.filtik.gronkhtv.twitch.helix.bits.model.BitsLeaderboardEntry
import de.filtik.gronkhtv.twitch.helix.http.model.array.CollectionResponse
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


/**
 * A collection response that contains one or multiple bits leaderboard entries.
 * @constructor Creates a new bits leaderboard response object.
 */
class BitsLeaderboardResponse(httpResponse: HttpResponse) :
    CollectionResponse<BitsLeaderboardEntry>(httpResponse) {

    override val helixArrayDTO: HelixArrayDTO<BitsLeaderboardEntry> = runBlocking {
        httpResponse.receive<HelixArrayDTO<BitsLeaderboardEntry>>()
    }
}