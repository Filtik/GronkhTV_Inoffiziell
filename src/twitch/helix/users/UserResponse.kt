package de.filtik.gronkhtv.twitch.helix.users

import de.filtik.gronkhtv.twitch.helix.http.model.array.HelixArrayDTO
import de.filtik.gronkhtv.twitch.helix.http.model.array.ScrollableResponse
import de.filtik.gronkhtv.twitch.helix.http.model.array.SingleResponse
import de.filtik.gronkhtv.twitch.helix.users.model.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking


class UserResponse(httpResponse: HttpResponse) : SingleResponse<User>(httpResponse) {
    override val helixArrayDTO: HelixArrayDTO<User> = runBlocking {
        httpResponse.receive()
    }
}


class UsersResponse(httpResponse: HttpResponse, httpClient: HttpClient) :
    ScrollableResponse<User>(httpResponse, httpClient) {
    override val helixArrayDTO: HelixArrayDTO<User> = runBlocking {
        httpResponse.receive()
    }

    override suspend fun nextPage(): UsersResponse? =
        nextPageHttpResponse()?.let {
            UsersResponse(
                it, httpClient
            )
        }
}

