package de.filtik.gronkhtv.twitch.helix.entitlements

import de.filtik.gronkhtv.twitch.helix.entitlements.model.EntitlementUrl
import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.SingleResponse
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


/**
 * A collection response that can contain a single entitlement grant object.
 * @constructor Creates a new entitlements grant url response object.
 */
class EntitlementGrantUrlResponse(httpResponse: HttpResponse) : SingleResponse<EntitlementUrl>(httpResponse) {

    override val helixArrayDTO: HelixArrayDTO<EntitlementUrl> = runBlocking {
        httpResponse.receive<HelixArrayDTO<EntitlementUrl>>()
    }
}