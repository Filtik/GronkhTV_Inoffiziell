package de.filtik.gronkhtv.twitch.helix.http

import de.filtik.gronkhtv.twitch.helix.auth.basic.default
import de.filtik.gronkhtv.twitch.helix.auth.model.AuthCredentials
import de.filtik.gronkhtv.twitch.helix.auth.model.OAuthCredentials
import de.filtik.gronkhtv.twitch.helix.auth.oauth.oauth
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

/**
 * An abstract service class where the configuration of the HTTP client happens.
 * @constructor Creates a new resource service given an HTTP client.
 * @param httpClient The HTTP client used to perform HTTP requests.
 */
abstract class ResourceService(
    protected val httpClient: HttpClient
) {
    /**
     * Creates a new resource service object given some [AuthCredentials].
     * Uses an [Apache] engine and a [KotlinxSerializer] for the JSON de-/serialization.
     * @param credentials The credentials necessary for authenticating the requests made by the HTTP client.
     */
    constructor(
        credentials: AuthCredentials
    )
            : this(HttpClient(Apache) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Auth) {
            if (credentials is OAuthCredentials) {
                oauth {
                    clientId = credentials.clientId
                    clientKey = credentials.clientKey
                    token = credentials.token
                }
            } else {
                default {
                    clientId = credentials.clientId
                    clientKey = credentials.clientKey
                }
            }
        }
    })

    companion object {
        const val BASE_URL = "https://api.twitch.tv/helix"
    }

}