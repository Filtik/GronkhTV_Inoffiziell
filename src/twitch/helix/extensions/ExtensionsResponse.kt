package de.filtik.gronkhtv.twitch.helix.extensions

import de.filtik.gronkhtv.twitch.helix.extensions.model.Extension
import de.filtik.gronkhtv.twitch.helix.http.model.array.CollectionResponse
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

/**
 * A collection response that contains one or multiple extension objects.
 * @constructor Creates a new extensions response object.
 */
class ExtensionsResponse(httpResponse: HttpResponse) :
    CollectionResponse<Extension>(httpResponse) {
    override val helixArrayDTO: HelixArrayDTO<Extension> = runBlocking {
        httpResponse.receive<HelixArrayDTO<Extension>>()
    }
}