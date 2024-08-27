package de.filtik.gronkhtv.twitch.helix.moderation

import de.filtik.gronkhtv.twitch.helix.http.model.array.CollectionResponse
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.moderation.model.AutoModMessageStatus
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

class AutoModResponse(httpResponse: HttpResponse) :
    CollectionResponse<AutoModMessageStatus>(httpResponse) {
    override val helixArrayDTO: HelixArrayDTO<AutoModMessageStatus> = runBlocking {
        httpResponse.receive<HelixArrayDTO<AutoModMessageStatus>>()
    }
}