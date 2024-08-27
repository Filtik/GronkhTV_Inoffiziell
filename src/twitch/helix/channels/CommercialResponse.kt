package de.filtik.gronkhtv.twitch.helix.channels

import de.filtik.gronkhtv.twitch.helix.channels.model.commercial.Commercial
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.SingleResponse
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


/**
 * A collection response that holds a single commercial resource.
 * @constructor Creates a new commercial response response object.
 */
class CommercialResponse(httpResponse: HttpResponse) : SingleResponse<Commercial>(httpResponse) {

    override val helixArrayDTO: HelixArrayDTO<Commercial> = runBlocking {
        httpResponse.receive<HelixArrayDTO<Commercial>>()
    }
}