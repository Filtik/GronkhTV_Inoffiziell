package de.filtik.gronkhtv.twitch.helix.auth.model.request


import de.filtik.gronkhtv.twitch.helix.auth.model.AuthScope
import de.filtik.gronkhtv.twitch.helix.shared.Mappable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Model used as payload for requesting a user access token.
 * @constructor Creates a new OAuth user token request model.
 * @param clientId The client ID of the Twitch application.
 * @param redirectUri The URI where the user will be redirected to after authenticating the app.
 * @param responseType The type of token retrieved, will always be *token*.
 * @param scopes The list of scopes that will be authorized.
 * @param forceVerify Specifies whether the user should be re-prompted for authorization. If this is true, the user always is prompted to confirm authorization.
 * @param state A unique token, generated by your application. This is an OAuth 2.0 opaque value, used to avoid CSRF attacks.
 */

@Serializable
data class OauthUserTokenRequestModel(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("redirect_uri")
    val redirectUri: String,
    @SerialName("response_type")
    val responseType: String = "token",
    @Serializable(with = AuthScopeListSerializer::class)
    @SerialName("scope")
    val scopes: List<AuthScope>,
    @SerialName("force_verify")
    val forceVerify: Boolean? = null,
    @SerialName("state")
    val state: String? = null
) : Mappable {

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "client_id" to clientId,
            "redirect_uri" to redirectUri,
            "response_type" to responseType,
            "scope" to scopes,
            "force_verify" to forceVerify,
            "state" to state
        )
    }
}

/**
 * Serializer class used to convert [String] lists to [AuthScope] lists.
 */
internal object AuthScopeListSerializer : KSerializer<List<AuthScope>> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("AuthScopeList", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<AuthScope> {
        throw NotImplementedError()
    }

    override fun serialize(encoder: Encoder, value: List<AuthScope>) {
        val strBuilder = StringBuilder()
        value.forEach {
            strBuilder.append("${it.scope} ")
        }
        encoder.encodeString(strBuilder.trim().toString())
    }
}